package com.longyan.policy.mapper;

import com.longyan.policy.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MessageMapper {

    List<Message> findUserMessage(Integer userId);

    Integer insertUserMessage(Message message);

    Integer updateUserMessage(Message message);
}
