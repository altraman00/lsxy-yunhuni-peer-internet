package com.lsxy.area.server.mq;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by Administrator on 2016/12/20.
 */
public class CdrEvent extends AbstractMQEvent {

    private String voiceCdr;

    private String callCenterId;

    public CdrEvent(){}

    public CdrEvent(String voiceCdr,String callCenterId){
        this.voiceCdr = voiceCdr;
        this.callCenterId = callCenterId;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AREA_SERVER;
    }

    public String getVoiceCdr() {
        return voiceCdr;
    }

    public void setVoiceCdr(String voiceCdr) {
        this.voiceCdr = voiceCdr;
    }

    public String getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(String callCenterId) {
        this.callCenterId = callCenterId;
    }
}
