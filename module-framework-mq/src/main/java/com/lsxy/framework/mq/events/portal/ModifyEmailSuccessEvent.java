package com.lsxy.framework.mq.events.portal;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * 修改邮件完成
 * Created by liups on 2016/7/26.
 */
public class ModifyEmailSuccessEvent extends AbstractMQEvent {
    private String accountId;//用户id
    private String email;//目标邮箱

    public ModifyEmailSuccessEvent() {
    }

    public ModifyEmailSuccessEvent(String accountId, String email) {
        this.accountId = accountId;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
