package com.lsxy.framework.api.events;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by liups on 2016/7/26.
 */
public class RegisterSuccessEvent extends AbstractMQEvent {
    private String accountId;

    public RegisterSuccessEvent() {
    }

    public RegisterSuccessEvent(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String getTopicName() {
        return "topic_framework_account";
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
