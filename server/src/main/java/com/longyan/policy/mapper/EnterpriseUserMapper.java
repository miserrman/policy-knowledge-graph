package com.longyan.policy.mapper;

import com.longyan.policy.domain.EnterpriseUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface EnterpriseUserMapper {

    EnterpriseUser findEnterpriseUserByUserId(Integer userId);

}
