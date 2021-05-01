package com.longyan.policy.service;

import com.longyan.policy.domain.PolicyContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PolicyContentService {
    @Autowired
    MongoTemplate mongoTemplate;

    public PolicyContent insertPolicyContent(PolicyContent policyContent) {
        PolicyContent res = mongoTemplate.insert(policyContent, "policyContent");
        if (res != null) {
            return res;
        }
        else {
            return null;
        }
    }

    public PolicyContent findPolicyContent(Integer id) {
        Query query = Query.query(Criteria.where("policy_id").is(id));
        List<PolicyContent> policyContents  = mongoTemplate.find(query, PolicyContent.class, "policyContent");
        if (policyContents.size() > 0) {
            return policyContents.get(0);
        }
        else {
            return null;
        }
    }

    public void deletePolicyContent(Integer id) {
        Query query = Query.query(Criteria.where("policy_id").is(id));
        mongoTemplate.remove(query, "policyContent");
    }
}
