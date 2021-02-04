package com.longyan.policy.mapper;

import com.longyan.policy.domain.PolicyTitle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PolicyTitleMapperTest {

    @Autowired
    PolicyTitleMapper policyTitleMapper;

    @Test
    void findPolicyTitleByType() {
    }

    @Test
    void findAllPolicyTitle() {
        List<PolicyTitle> result = policyTitleMapper.findAllPolicyTitle();
        for(PolicyTitle r : result) {
            System.out.println(r.getDepart());
        }
    }
}
