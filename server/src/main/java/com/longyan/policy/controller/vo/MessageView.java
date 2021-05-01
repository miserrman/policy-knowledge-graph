package com.longyan.policy.controller.vo;

import com.longyan.policy.domain.PolicyTitle;

import java.util.List;

public class MessageView {

    private List<PolicyTitle> readMessage;

    private List<PolicyTitle> unReadMessage;

    public List<PolicyTitle> getReadMessage() {
        return readMessage;
    }

    public void setReadMessage(List<PolicyTitle> readMessage) {
        this.readMessage = readMessage;
    }

    public List<PolicyTitle> getUnReadMessage() {
        return unReadMessage;
    }

    public void setUnReadMessage(List<PolicyTitle> unReadMessage) {
        this.unReadMessage = unReadMessage;
    }
}
