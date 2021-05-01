package com.longyan.policy.service;

import com.longyan.policy.controller.vo.MessageView;
import com.longyan.policy.dao.GraphDao;
import com.longyan.policy.domain.*;
import com.longyan.policy.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfoService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private PolicyTitleMapper policyTitleMapper;

    @Autowired
    private GraphDao graphDao;

    @Autowired
    private SubscribeMapper subscribeMapper;

    @Autowired
    private EnterpriseUserMapper enterpriseUserMapper;

    @Autowired
    private EnterpriseMapper enterpriseMapper;

    private boolean isUserSubscribeAdapt(Subscribe subscribe, PolicyTitle policyTitle) {
        List<String> departIds = parseIds(subscribe.getDepartIds());
        if (departIds.contains(String.valueOf(policyTitle.getDepartId()))) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isUserCompanyAdapt(Enterprise enterprise, PolicyTitle policyTitle) {
        return false;
    }

    private List<String> parseIds(String ids) {
        String[] idList = ids.split(",");
        return Arrays.asList(idList);
    }

    private String stringfyIds(List<PolicyTitle> policyTitles) {
        List<String> idList = policyTitles.stream().map(x -> String.valueOf(x.getId())).collect(Collectors.toList());
        String idRes = String.join(",", idList);
        return idRes;
    }

    public MessageView getUserMessage(Integer userId, Integer dayLimit) {
        //生成需要推荐用户的政策，范围为dayLimit
        Date date = new Date();
        Date startTime = new Date(date.getTime() - dayLimit * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTimeSearch = simpleDateFormat.format(startTime);
        String endTimeSearch = simpleDateFormat.format(date);
        List<PolicyTitle> policyTitleList = policyTitleMapper.findPolicyTitleByDate(startTimeSearch, endTimeSearch);
        List<Message> messages = messageMapper.findUserMessage(userId);
        List<String> unReadPolicyTitles = new ArrayList<>();
        List<String> readPolicyTitles = new ArrayList<>();
        Message unReadMessage = null;
        for (Message message : messages) {
            if (message.getIsRead() == 0) {
                unReadMessage = message;
                unReadPolicyTitles.addAll(parseIds(message.getPolicyIds()));
            }
            else {
                readPolicyTitles.addAll(parseIds(message.getPolicyIds()));
            }
        }
        List<String> policyTitleIds = policyTitleList.stream().map(x -> String.valueOf(x.getId()))
                                                              .collect(Collectors.toList());
        for (int i = policyTitleIds.size() - 1; i >= 0; i--) {
            if (readPolicyTitles.contains(policyTitleIds.get(i)) || unReadPolicyTitles.contains(policyTitleIds.get(i))) {
                policyTitleIds.remove(i);
            }
        }
        List<PolicyTitle> policyTitleRes = new ArrayList<>();
        if (policyTitleIds.size() > 0) {
            policyTitleRes = policyTitleMapper.batchFindPolicyTitlesByIds(policyTitleIds);
        }
        Subscribe subscribe = subscribeMapper.findUserSubscribe(userId);
        EnterpriseUser enterpriseUser = enterpriseUserMapper.findEnterpriseUserByUserId(userId);
        Enterprise enterprise = enterpriseMapper.findEnterpriseById(enterpriseUser.getEnterpriseId());
        if (subscribe != null) {
            policyTitleRes = policyTitleRes.stream().filter(x -> isUserSubscribeAdapt(subscribe, x) || isUserCompanyAdapt(enterprise, x))
                    .collect(Collectors.toList());
        }
        else {
            policyTitleRes = new ArrayList<>();
        }

        List<PolicyTitle> extraUnreadPolicyTitles = new ArrayList<>();
        if (unReadPolicyTitles.size() > 0) {
            extraUnreadPolicyTitles = policyTitleMapper.batchFindPolicyTitlesByIds(unReadPolicyTitles);
        }
        //更新message表，把未读的政策update到表里
        if (policyTitleRes.size() > 0) {
            if (unReadMessage == null) {
                Message message = new Message();
                message.setMessage("请读政策");
                message.setPolicyIds(stringfyIds(policyTitleRes));
                message.setUserId(userId);
                message.setIsRead(0);
                messageMapper.insertUserMessage(message);
            }
            else {
                String ids = stringfyIds(policyTitleRes);
                unReadMessage.setPolicyIds(unReadMessage.getPolicyIds() + "," + ids);
                messageMapper.updateUserMessage(unReadMessage);
            }
        }
        policyTitleRes.addAll(extraUnreadPolicyTitles);
        MessageView messageView = new MessageView();
        messageView.setUnReadMessage(policyTitleRes);
        if (readPolicyTitles.size() > 0) {
            messageView.setReadMessage(policyTitleMapper.batchFindPolicyTitlesByIds(readPolicyTitles));
        }
        return messageView;
    }
}
