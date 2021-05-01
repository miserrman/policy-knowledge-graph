package com.longyan.policy.controller;

import com.longyan.policy.domain.User;
import com.longyan.policy.service.UserService;
import com.longyan.policy.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/user")
    public Object register(@RequestBody User user) {
        try {
            User result = userService.addUser(user);
            result.setPassword(null);
            return ResponseUtil.ok(result);
        }catch (RuntimeException e) {
            return ResponseUtil.fail(500, e.getMessage());
        }
    }

    @GetMapping("/user")
    public Object login(String userName, String password) {
        try {
            User user = userService.login(userName, password);
            return ResponseUtil.ok(user);
        }catch (RuntimeException e) {
            return ResponseUtil.fail(500, e.getMessage());
        }
    }

    @GetMapping("/manage/user")
    public Object getAllUsers(@RequestHeader("user-info")String role) {
        int r = Integer.parseInt(role);
        if (r == 0) {
            return ResponseUtil.fail(500, "用户无权限");
        }
        else {
            return ResponseUtil.ok(userService.getAllUsers());
        }
    }

    @GetMapping("/user/enterprise")
    public Object getUserEnterprise(@RequestHeader("userId")Integer userId) {
        return ResponseUtil.ok(userService.getUserEnterprise(userId));
    }
}
