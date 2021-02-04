package com.longyan.policy.controller;

import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.service.PolicyTitleService;
import com.longyan.policy.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
