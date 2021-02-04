package com.longyan.policy.mapper;

import com.longyan.policy.domain.PolicyTitle;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Component
public interface PolicyTitleMapper {

    List<PolicyTitle> findPolicyTitleByType(Integer type);

    List<PolicyTitle> findAllPolicyTitle();

}
