package com.longyan.policy.controller;

import com.longyan.policy.controller.vo.PolicyView;
import com.longyan.policy.controller.vo.PublishView;
import com.longyan.policy.controller.vo.SubscribeView;
import com.longyan.policy.domain.PolicyContent;
import com.longyan.policy.domain.PolicyManage;
import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.domain.Subscribe;
import com.longyan.policy.mapper.PolicyManageMapper;
import com.longyan.policy.service.*;
import com.longyan.policy.thrift.client.RpcPyService;
import com.longyan.policy.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
public class PolicyController {

    @Autowired
    PolicyTitleService policyTitleService;

    @Autowired
    PolicyContentService policyContentService;

    @Autowired
    SubscribeService subscribeService;

    @Autowired
    PolicyManageService policyManageService;

    @GetMapping("/policy/title")
    public Object getAllPolicyTitles(Integer type) {
        List<PolicyTitle> policyTitleList = policyTitleService.findPolicyByType(type);
        return ResponseUtil.ok(policyTitleList);
    }

    @GetMapping("/policy/{id}/relevant")
    public Object getRelevantPolicy(@PathVariable Integer id) {
        return ResponseUtil.ok(policyTitleService.getRelevant(id));
    }

    @GetMapping("/policy/title/{id}")
    public Object getPolicyTitleById(@PathVariable Integer id) {
        PolicyTitle policyTitle = policyTitleService.getPolicyTitleById(id);
        return policyTitle;
    }

    @GetMapping("/policy/title/search")
    public Object searchPolicyTitle(String title, Integer departId, String startDate, String endDate) {
        PolicyTitle policyTitle = new PolicyTitle();
        if (title != null) {
            policyTitle.setTitle(title);
        }
        if (departId != null) {
            policyTitle.setDepartId(departId);
        }
        if (startDate != null || endDate != null) {
            if (startDate == null) {
                startDate = "2010-1-1";
            }
            if (endDate == null) {
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                endDate = simpleDateFormat.format(date);
            }
            policyTitle.setDate(startDate + ":" + endDate);
        }
        List<PolicyTitle> policyTitleList = policyTitleService.searchPolicyTitle(policyTitle);
        return policyTitleList;
    }

    @GetMapping("/policy/content")
    public Object insertPolicyContent() {
        PolicyContent policyContent = new PolicyContent();
        policyContent.setPolicy_id(12);
        policyContent.setContent("cdscsdcsdcsdc");
        policyContent.setCondition("csdcdcdscsdc");
        policyContentService.insertPolicyContent(policyContent);
        return ResponseUtil.ok();
    }

    @PostMapping("/policy")
    public Object addPolicy(@RequestBody PublishView publishView) {
        System.out.println(publishView.getDepart());
        Integer id = policyTitleService.addPolicy(publishView.getDepart(), publishView.getTitle(), publishView.getContent());
        PolicyTitle policyTitle = policyTitleService.getPolicyTitleById(id);
        PolicyContent policyContent = policyContentService.findPolicyContent(id);
        List<String> tagList = policyTitleService.extractAllTags(id);
        PolicyView policyView = new PolicyView();
        policyView.setId(id);
        policyView.setTagList(tagList);
        policyView.setPolicyContent(policyContent);
        policyView.setPolicyTitle(policyTitle);
        return ResponseUtil.ok(policyView);
    }

    @GetMapping("/policy/{id}")
    public Object getPolicyById(@PathVariable Integer id) {
        PolicyTitle policyTitle = policyTitleService.getPolicyTitleById(id);
        PolicyContent policyContent = policyContentService.findPolicyContent(id);
        List<String> tagList = policyTitleService.extractAllTags(id);
        PolicyView policyView = new PolicyView();
        policyView.setId(id);
        policyView.setTagList(tagList);
        policyView.setPolicyContent(policyContent);
        policyView.setPolicyTitle(policyTitle);
        return ResponseUtil.ok(policyView);
    }

    @GetMapping("/manage/policy")
    public Object getPolicyList(@RequestHeader("role")String role, Integer page, Integer limit) {
        try {
            Integer r = Integer.parseInt(role);
            if (r.equals(1))
                return ResponseUtil.ok(policyTitleService.findAllPolicyTitles(page, limit));
            else
                return ResponseUtil.fail(500, "用户无权限");
        }catch(RuntimeException e) {
            return ResponseUtil.fail(500, "请求格式错误");
        }
    }

    @GetMapping("/test/policy")
    public Object extractPolicy() {
        policyTitleService.extractPolicyDepart();
        return null;
    }

    @PostMapping("/policy/subscribe")
    public Object subscribe(@RequestHeader("userId")Integer userId, @RequestBody Subscribe subscribe) {
//        try {
            System.out.println(subscribe.getRegions());
            subscribe.setUserId(userId);
            Subscribe res = subscribeService.addOrUpdateSubscribe(subscribe);

            return ResponseUtil.ok(res);
//        }catch (RuntimeException e) {
//            return ResponseUtil.fail(500, e.getMessage());
//        }
    }

    @GetMapping("/policy/subscribe")
    public Object findUserSubscribe(@RequestHeader("userId")Integer userId) {
        SubscribeView subscribeView = subscribeService.getUserSubscribe(userId);
        return ResponseUtil.ok(subscribeView);
    }

    @GetMapping("/policy/subscribe/user")
    public Object generateUserSubscribePolicyTitles(@RequestHeader("userId")Integer userId) {
        List<PolicyTitle> policyTitleList = policyTitleService.getUserSubscribe(userId);
        return ResponseUtil.ok(policyTitleList);
    }

    @GetMapping("/policy/graph")
    public Object getPolicyGraph() {
        return ResponseUtil.ok(policyTitleService.getPolicyGraph());
    }

    /**
     * 测试接口
     */
    @GetMapping("/policy/test")
    public Object testPolicy() {
        policyContentService.deletePolicyContent(918);
        return null;
    }

    @GetMapping("/policy/create/info")
    public Object getPolicyCreateInfo() {
        return ResponseUtil.ok(policyTitleService.getCreatePolicyInfo());
    }

    @GetMapping("/policy/manage")
    public Object getManagePolicy(String title, Integer departId,
                                  Integer code, String startDate, String endDate) {
        return ResponseUtil.ok(policyManageService.searchPolicyManage(departId, title, code, startDate, endDate));
    }

    @PostMapping("/policy/manage")
    public Object updateManagePolicy(@RequestBody PolicyManage policyManage) {
        policyManageService.updatePolicyManage(policyManage);
        return ResponseUtil.ok();
    }

    @GetMapping("/enterprise/recommend")
    public Object getEnterpriseRecommend(@RequestHeader("userId")Integer userId) {
        return ResponseUtil.ok(policyTitleService.enterpriseRecommendPolicyTitles(userId));
    }

}
