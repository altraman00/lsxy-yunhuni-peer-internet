package com.lsxy.framework.mq.events.test;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by tandy on 16/9/27.
 * 测试使用
 */
public class EchoEvent extends AbstractMQEvent {
    private int echoId;

    public EchoEvent(){}

    public int getEchoId() {
        return echoId;
    }

    public void setEchoId(int echoId) {
        this.echoId = echoId;
    }

    public EchoEvent(int echoId){
        this.echoId = echoId;
    }
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_APP_PORTAL;
    }
}
