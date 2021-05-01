package com.longyan.policy.controller.vo;

import com.longyan.policy.domain.PolicyDepart;
import com.longyan.policy.domain.Subscribe;

import java.util.List;

public class SubscribeView {

    private List<PolicyDepart> policyDepartList;

    private List<String> regionList;

    private List<String> organizations;

    private List<String> industry;

    private Subscribe subscribe;

    public List<PolicyDepart> getPolicyDepartList() {
        return policyDepartList;
    }

    public void setPolicyDepartList(List<PolicyDepart> policyDepartList) {
        this.policyDepartList = policyDepartList;
    }

    public List<String> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<String> regionList) {
        this.regionList = regionList;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public List<String> getIndustry() {
        return industry;
    }

    public void setIndustry(List<String> industry) {
        this.industry = industry;
    }

    public Subscribe getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Subscribe subscribe) {
        this.subscribe = subscribe;
    }
}
