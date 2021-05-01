package com.longyan.policy.mapper;

import com.longyan.policy.domain.Industry;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface IndustryMapper {

    Industry findIndustryById(Integer id);

}
