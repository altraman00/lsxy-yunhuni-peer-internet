package com.lsxy.framework.api.events;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by liups on 2016/7/26.
 */
public class RegisterSuccessEvent extends AbstractMQEvent {
    private String accountId;

    @Override
    public String getTopicName() {
        return "topic_framework_account";
    }

}
