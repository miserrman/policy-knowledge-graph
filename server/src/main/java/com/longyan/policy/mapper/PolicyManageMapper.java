package com.longyan.policy.mapper;

import com.longyan.policy.domain.PolicyManage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PolicyManageMapper {

    Integer insertPolicyManage(PolicyManage policyManage);

    PolicyManage getPolicyManage(Integer policyId);

    Integer updatePolicyManage(PolicyManage policyManage);

    List<PolicyManage> getPolicyManageByCode(Integer code);

    Integer deletePolicyManage(Integer policyId);
}
