package com.lsxy.framework.mq.events.callcenter;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/11/9.
 */
public class DeleteConditionEvent extends AbstractMQEvent {

    private String conditionId;

    private String tenantId;

    private String appId;

    private String channelId;

    public DeleteConditionEvent(){
    }

    public DeleteConditionEvent(String conditionId, String tenantId, String appId,String channelId){
        this.conditionId = conditionId;
        this.tenantId = tenantId;
        this.appId = appId;
        this.channelId = channelId;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_CALL_CENTER;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
