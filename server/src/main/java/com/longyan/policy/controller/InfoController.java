package com.longyan.policy.controller;

import com.longyan.policy.service.InfoService;
import com.longyan.policy.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/info/user")
    public Object getUserMessage(@RequestHeader("userId") Integer userId) {
        return ResponseUtil.ok(infoService.getUserMessage(userId, 3));
    }
}
