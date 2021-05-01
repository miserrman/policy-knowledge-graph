package com.longyan.policy.mapper;

import com.longyan.policy.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {

    Integer addUser(User user);

    User findUserById(Integer userId);

    User findUserByUserName(String userName);

    User updateUserInfo(User user);

    List<User> getAllUsers();

}
