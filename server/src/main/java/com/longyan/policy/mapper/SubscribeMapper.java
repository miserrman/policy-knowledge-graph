package com.longyan.policy.mapper;

import com.longyan.policy.domain.Subscribe;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SubscribeMapper {

    Integer insertSubscribe(Subscribe subscribe);

    Subscribe findUserSubscribe(Integer userId);

    Integer updateUserSubscribe(Subscribe subscribe);

}
