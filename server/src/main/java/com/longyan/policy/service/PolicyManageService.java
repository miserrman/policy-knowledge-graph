package com.longyan.policy.service;

import com.longyan.policy.controller.vo.PolicyManageView;
import com.longyan.policy.domain.PolicyManage;
import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.mapper.PolicyManageMapper;
import com.longyan.policy.mapper.PolicyTitleMapper;
import com.longyan.policy.util.RecommendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PolicyManageService {

    @Autowired
    private PolicyTitleMapper policyTitleMapper;

    @Autowired
    PolicyManageMapper policyManageMapper;

    public void updatePolicyManage(PolicyManage policyManage) {
        if (policyManage.getCode() != 0) {
            PolicyManage check = policyManageMapper.getPolicyManage(policyManage.getPolicyId());
            if (check != null) {
                policyManageMapper.updatePolicyManage(policyManage);
                return;
            }
            policyManageMapper.insertPolicyManage(policyManage);
        }
        else {
            policyManageMapper.deletePolicyManage(policyManage.getPolicyId());
        }
    }

    public PolicyManage getPolicyManage(Integer policyId) {
        return policyManageMapper.getPolicyManage(policyId);
    }

    public List<PolicyManage> getPolicyManageByCode(Integer code) {
        return policyManageMapper.getPolicyManageByCode(code);
    }

    public List<PolicyManageView> searchPolicyManage(Integer departId, String title, Integer code,
                                                     String startDate, String endDate) {
        List<PolicyTitle> res;
        List<PolicyManageView> finalRes = new ArrayList<>();
        if (departId != null || title != null) {
            PolicyTitle policyTitle = new PolicyTitle();
            policyTitle.setDepartId(departId);
            policyTitle.setTitle(title);
            res = policyTitleMapper.searchPolicyTitle(policyTitle);
        }
        else {
            res = policyTitleMapper.findAllPolicyTitle();
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
            List<PolicyTitle> policyTitleList = policyTitleMapper.findPolicyTitleByDate(startDate, endDate);
            res = RecommendUtil.selectAdaptPolicyTitles(res, policyTitleList);
        }
        if (code != null) {
            //显示隐藏
            List<Integer> resId = res.stream().map(PolicyTitle::getId).collect(Collectors.toList());
            List<PolicyManage> policyManageList = getPolicyManageByCode(code);
            for (PolicyManage policyManage : policyManageList) {
                int index = resId.indexOf(policyManage.getPolicyId());
                if (index >= 0) {
                    PolicyManageView policyManageView = new PolicyManageView();
                    policyManageView.setPolicyTitle(res.get(index));
                    policyManageView.setPolicyManage(policyManage);
                    finalRes.add(policyManageView);
                }
            }
        }
        else {
            //不显示隐藏，根据code id对置顶进行排序
            List<Integer> resId = res.stream().map(PolicyTitle::getId).collect(Collectors.toList());
            List<PolicyManage> policyManageList = getPolicyManageByCode(2);
            for (PolicyManage policyManage : policyManageList) {
                int index = resId.indexOf(policyManage.getPolicyId());
                if (index >= 0) {
                    resId.remove(index);
                    res.remove(index);
                }
            }
            policyManageList = getPolicyManageByCode(1);
            for (PolicyManage policyManage : policyManageList) {
                int index = resId.indexOf(policyManage.getPolicyId());
                if (index >= 0) {
                    PolicyManageView policyManageView = new PolicyManageView();
                    policyManageView.setPolicyTitle(res.get(index));
                    policyManageView.setPolicyManage(policyManage);
                    finalRes.add(policyManageView);
                    resId.remove(index);
                    res.remove(index);
                }
            }
            finalRes.sort(new Comparator<PolicyManageView>() {
                @Override
                public int compare(PolicyManageView o1, PolicyManageView o2) {
                    if (o1.getPolicyManage().getId() < o2.getPolicyManage().getPolicyId()) {
                        return 1;
                    }
                    else if(o1.getPolicyManage().getId() > o2.getPolicyManage().getPolicyId()) {
                        return -1;
                    }
                    else {
                        return 0;
                    }
                }
            });
            for (PolicyTitle policyTitle : res) {
                PolicyManageView policyManageView = new PolicyManageView();
                policyManageView.setPolicyTitle(policyTitle);
                policyManageView.setPolicyManage(null);
                finalRes.add(policyManageView);
            }
        }
        return finalRes;
    }
}
