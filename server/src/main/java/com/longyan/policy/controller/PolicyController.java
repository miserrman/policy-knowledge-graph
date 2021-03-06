package com.longyan.policy.controller;

import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.service.PolicyTitleService;
import com.longyan.policy.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PolicyController {

    @Autowired
    PolicyTitleService policyTitleService;

    @GetMapping("/policy/title")
    public  Object getAllPolicyTitles(Integer type) {
        List<PolicyTitle> policyTitleList = policyTitleService.findPolicyByType(type);
        return ResponseUtil.ok(policyTitleList);
    }

    @GetMapping("/policy/title/{id}")
    public Object getPolicyTitleById(@PathVariable Integer id) {
        PolicyTitle policyTitle = policyTitleService.getPolicyTitleById(id);
        return policyTitle;
    }

    @GetMapping("/policy/title/search")
    public Object searchPolicyTitle(String title, String depart) {
        PolicyTitle policyTitle = new PolicyTitle();
        if (title != null) {
            policyTitle.setTitle(title);
        }
        if (depart != null) {
            policyTitle.setDepart(depart);
        }
        List<PolicyTitle> policyTitleList = policyTitleService.searchPolicyTitle(policyTitle);
        return policyTitleList;
    }

}
