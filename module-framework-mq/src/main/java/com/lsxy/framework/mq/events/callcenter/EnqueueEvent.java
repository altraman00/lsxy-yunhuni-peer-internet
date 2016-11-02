package com.lsxy.framework.mq.events.callcenter;

import com.lsxy.framework.mq.api.AbstractDelayMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/9/13.
 */
public class EnqueueEvent extends AbstractDelayMQEvent{

    private String key;

    private String tenantId;

    private String appId;

    private String callId;

    public EnqueueEvent(){}

    public EnqueueEvent(String tenantId, String appId, String callId, Integer delay){
        super(delay);
        this.tenantId = tenantId;
        this.appId = appId;
        this.callId = callId;
    }
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_CALL_CENTER;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
