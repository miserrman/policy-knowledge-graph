package com.longyan.policy.domain;

public class Message {

    private Integer id;

    private String policyIds;

    private String message;

    private Integer isRead;

    private Integer userId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPolicyIds() {
        return policyIds;
    }

    public void setPolicyIds(String policyIds) {
        this.policyIds = policyIds;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
