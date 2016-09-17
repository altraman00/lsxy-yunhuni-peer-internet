package com.lsxy.framework.mq.events.agentserver;

import com.lsxy.framework.mq.api.AbstractDelayMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/9/13.
 */
public class IVRPauseActionEvent extends AbstractDelayMQEvent{

    private String callId;

    public IVRPauseActionEvent(){}

    public IVRPauseActionEvent(String callId,Integer delay){
        super(delay);
        this.callId = callId;
    }
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AGENT_SERVER;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
