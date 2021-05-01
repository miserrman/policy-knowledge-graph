package com.longyan.policy.domain;
import org.springframework.data.mongodb.core.mapping.Document;

public class PolicyContent {

    private Integer policy_id;

    private String content;

    private String condition;

    public Integer getPolicy_id() {
        return policy_id;
    }

    public void setPolicy_id(Integer policy_id) {
        this.policy_id = policy_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}

