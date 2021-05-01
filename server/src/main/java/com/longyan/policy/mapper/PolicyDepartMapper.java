package com.longyan.policy.mapper;

import com.longyan.policy.domain.PolicyDepart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface PolicyDepartMapper {

    Integer insertDepart(PolicyDepart policyDepart);

    PolicyDepart findDepartByName(@Param("name") String name);

    List<PolicyDepart> findAllDeparts();

    PolicyDepart findDepartById(Integer id);

    List<PolicyDepart> findDepartByRegion(String region);
}
