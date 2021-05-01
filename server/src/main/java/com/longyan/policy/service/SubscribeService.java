package com.longyan.policy.service;

import com.longyan.policy.controller.vo.SubscribeView;
import com.longyan.policy.domain.PolicyDepart;
import com.longyan.policy.domain.Subscribe;
import com.longyan.policy.domain.User;
import com.longyan.policy.mapper.PolicyDepartMapper;
import com.longyan.policy.mapper.SubscribeMapper;
import com.longyan.policy.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscribeService {

    @Autowired
    private SubscribeMapper subscribeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PolicyDepartMapper policyDepartMapper;

    public Subscribe addOrUpdateSubscribe(Subscribe subscribe) {
        User user = userMapper.findUserById(subscribe.getUserId());
        Subscribe isSubscribe = subscribeMapper.findUserSubscribe(subscribe.getUserId());
        if (user == null) {
            throw new RuntimeException("部门信息错误");
        }
        List<String> departIdListStr = Arrays.asList(subscribe.getDepartIds().split(","));
        List<Integer> departIdList = departIdListStr.stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());
        for (Integer departId : departIdList) {
            PolicyDepart policyDepart = policyDepartMapper.findDepartById(departId);
            if (policyDepart == null) {
                throw new RuntimeException("部门信息错误");
            }
        }
        if (isSubscribe == null) {
            subscribeMapper.insertSubscribe(subscribe);
            return subscribe;
        }
        else {
            subscribeMapper.updateUserSubscribe(subscribe);
            return subscribe;
        }
    }

    public SubscribeView getUserSubscribe(Integer userId) {
        List<PolicyDepart> policyDepartList = policyDepartMapper.findAllDeparts();
        Set<String> regionSet = new HashSet<>();
        for (PolicyDepart policyDepart : policyDepartList) {
            regionSet.add(policyDepart.getRegion());
        }
        List<String> regionList = new ArrayList<>(regionSet);
        Subscribe subscribe = subscribeMapper.findUserSubscribe(userId);
        SubscribeView subscribeView = new SubscribeView();
        subscribeView.setSubscribe(subscribe);
        subscribeView.setPolicyDepartList(policyDepartList);
        subscribeView.setRegionList(regionList);
        return subscribeView;
    }
}
