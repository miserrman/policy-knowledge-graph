package com.longyan.policy.service;

import com.longyan.policy.domain.PolicyTitle;
import com.longyan.policy.mapper.PolicyTitleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyTitleService {

    @Autowired
    PolicyTitleMapper policyTitleMapper;

    /**
     * 可以用来查询所有政策标题，也可以通过type查
     * @return
     */
    public List<PolicyTitle> findPolicyByType(Integer type) {
        if (type == 0) {
            return policyTitleMapper.findAllPolicyTitle();
        }
        else {
            return policyTitleMapper.findPolicyTitleByType(type);
        }
    }

    public List<PolicyTitle> searchPolicyTitle(PolicyTitle policyTitle) {
        List<PolicyTitle> res = policyTitleMapper.searchPolicyTitle(policyTitle);
        return res;
    }

    public PolicyTitle getPolicyTitleById(Integer id) {
        PolicyTitle p = policyTitleMapper.findPolicyTitleById(id);
        return p;
    }
}
