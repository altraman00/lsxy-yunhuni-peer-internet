package com.lsxy.framework.mq.events.portal;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * 充值成功事件
 * Created by liups on 2017/1/13.
 */
public class RechargeSuccessEvent extends AbstractMQEvent {
    String tenantId;

    public RechargeSuccessEvent() {
    }

    public RechargeSuccessEvent(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_APP_PORTAL;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
