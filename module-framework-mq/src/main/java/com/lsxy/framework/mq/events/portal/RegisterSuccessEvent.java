package com.lsxy.framework.mq.events.portal;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * 用户注册，创建用户成功后的事件
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
        return MQTopicConstants.TOPIC_APP_PORTAL;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
