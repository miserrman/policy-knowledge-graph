package com.longyan.policy.service;

import com.longyan.policy.domain.PolicyContent;
import com.longyan.policy.domain.PolicyDepart;
import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.mapper.MessageMapper;
import com.longyan.policy.mapper.PolicyDepartMapper;
import com.longyan.policy.mapper.PolicyTitleMapper;
import com.longyan.policy.thrift.Policy;
import com.longyan.policy.thrift.client.RpcPyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WritableService {

    @Autowired
    RpcPyService rpcPyService;

    @Autowired
    PolicyDepartMapper policyDepartMapper;

    @Autowired
    PolicyTitleMapper policyTitleMapper;

    @Autowired
    PolicyContentService policyContentService;

    @Autowired
    MessageMapper messageMapper;

    public Integer addPolicy(String depart, String link, String date, String title, String content, String condition) {
        String[] departInfo = depart.split(",");
        String departName = departInfo[1];
        int level = 0;
        switch (departInfo[0]) {
            case "国家级":
                level = 0; break;
            case "省市级":
                level = 1; break;
            case "地区级":
                level = 2; break;
        }
        PolicyDepart policyDepart = policyDepartMapper.findDepartByName(departName);
        if (policyDepart == null) {
            policyDepart = new PolicyDepart();
            //这里有待改进
            if (level == 0) {
                policyDepart.setRegion(null);
            }
            policyDepart.setName(departName);
            policyDepart.setLevel(level);
            policyDepartMapper.insertDepart(policyDepart);
        }
        if (policyDepart.getId() == null) {
            throw new RuntimeException("部门信息异常");
        }
        PolicyTitle prepareInsertTitle = new PolicyTitle();
        prepareInsertTitle.setTitle(title);
        prepareInsertTitle.setDepartId(policyDepart.getId());
        prepareInsertTitle.setDate(date);
        prepareInsertTitle.setLink(link);
        int insertRes = policyTitleMapper.insertPolicyTitle(prepareInsertTitle);

        PolicyContent insertPolicyContent = null;
        if (insertRes > 0) {
            PolicyContent policyContent = new PolicyContent();
            policyContent.setPolicy_id(prepareInsertTitle.getId());
            policyContent.setContent(content);
            policyContent.setCondition(condition);
            insertPolicyContent = policyContentService.insertPolicyContent(policyContent);
        }
        if (insertRes > 0 && insertPolicyContent != null) {
            analysisPolicyContent(prepareInsertTitle.getId(), prepareInsertTitle.getTitle(), insertPolicyContent.getContent());
            return prepareInsertTitle.getId();
        }
        else {
            return null;
        }
    }

    @Scheduled(cron = "0 05 04 ? * *")
    public void spiderPolicyOnTime() {
        List<Policy> policyList = rpcPyService.spiderRecentlyPolicy();
        for (Policy policy : policyList) {
            String link = policy.policyTitle.link;
            PolicyTitle policyTitle = policyTitleMapper.findPolicyTitleByLink(link);
            if (policyTitle != null) {
                continue;
            }
            if (policy.policyTitle.depart == "" || policy.policyTitle.depart == null) {
                continue;
            }
            else {
                String[] departInfo = policy.policyTitle.depart.split(",");
                if (departInfo.length < 2) {
                    throw new RuntimeException("部门信息错误");
                }
                Integer id = addPolicy(policy.policyTitle.depart, policy.policyTitle.link, policy.policyTitle.date,
                        policy.policyTitle.title, policy.policyContent.content, policy.policyContent.condition);
                if (id != null) {
                    throw new RuntimeException("插入异常");
                }
            }
        }
    }

    public boolean analysisPolicyContent(Integer articleId, String title, String content) {
        Policy policy = null;
        policy = rpcPyService.analysisPolicy(articleId, title, content);
        return true;
    }
}
