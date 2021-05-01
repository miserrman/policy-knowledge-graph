package com.longyan.policy.service;

import com.longyan.policy.dao.EnterpriseDao;
import com.longyan.policy.domain.Enterprise;
import com.longyan.policy.domain.User;
import com.longyan.policy.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EnterpriseDao enterpriseDao;

    public User addUser(User user) {
        if (user.getUserName() == null) {
            throw new RuntimeException("用户名不能为空");
        }
        else if (user.getPassword() == null) {
            throw new RuntimeException("密码不能为空");
        }
        else if (userMapper.findUserByUserName(user.getUserName()) != null) {
            throw new RuntimeException("用户名重复");
        }
        String pwd = DigestUtils.md5DigestAsHex((user.getUserName() + user.getPassword()).getBytes(StandardCharsets.UTF_8));
        user.setPassword(pwd);
        user.setId(null);
        Integer result = userMapper.addUser(user);
        user.setPassword(null);
        return user;
    }

    public User login(String userName, String password) {
        User user = userMapper.findUserByUserName(userName);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        String pwd = DigestUtils.md5DigestAsHex((userName + password).getBytes(StandardCharsets.UTF_8));
        if (pwd.equals(user.getPassword())) {
            user.setPassword(null);
            return user;
        }
        else {
            throw new RuntimeException("密码错误");
        }
    }

    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    public Enterprise getUserEnterprise(Integer userId) {
        return enterpriseDao.getEnterpriseByUserId(userId);
    }
}
