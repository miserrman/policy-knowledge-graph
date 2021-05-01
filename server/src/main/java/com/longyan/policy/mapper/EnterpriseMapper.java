package com.longyan.policy.mapper;

import com.longyan.policy.domain.Enterprise;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface EnterpriseMapper {

    Enterprise findEnterpriseById(Integer id);

    List<Enterprise> findAllEnterprises();
}
