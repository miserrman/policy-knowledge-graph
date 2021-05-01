package com.longyan.policy.controller.vo;

import com.longyan.policy.domain.PolicyContent;
import com.longyan.policy.domain.PolicyTitle;

import java.util.List;

public class PolicyView {

    private Integer id;

    private PolicyTitle policyTitle;

    private PolicyContent policyContent;

    private List<String> tagList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public PolicyTitle getPolicyTitle() {
        return policyTitle;
    }

    public void setPolicyTitle(PolicyTitle policyTitle) {
        this.policyTitle = policyTitle;
    }

    public PolicyContent getPolicyContent() {
        return policyContent;
    }

    public void setPolicyContent(PolicyContent policyContent) {
        this.policyContent = policyContent;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }
}
