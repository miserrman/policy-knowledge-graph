package com.longyan.policy.controller.vo;

import com.longyan.policy.domain.PolicyManage;
import com.longyan.policy.domain.PolicyTitle;

import java.util.List;

public class PolicyManageView {

    private PolicyTitle policyTitle;

    private PolicyManage policyManage;

    public PolicyTitle getPolicyTitle() {
        return policyTitle;
    }

    public void setPolicyTitle(PolicyTitle policyTitle) {
        this.policyTitle = policyTitle;
    }

    public PolicyManage getPolicyManage() {
        return policyManage;
    }

    public void setPolicyManage(PolicyManage policyManage) {
        this.policyManage = policyManage;
    }
}
