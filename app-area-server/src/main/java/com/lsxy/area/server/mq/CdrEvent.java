package com.lsxy.area.server.mq;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/20.
 */
public class CdrEvent extends AbstractMQEvent {

    private VoiceCdr voiceCdr;

    private String callCenterId;

    public CdrEvent(){}

    public CdrEvent(VoiceCdr voiceCdr,String callCenterId){
        this.voiceCdr = voiceCdr;
        this.callCenterId = callCenterId;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AREA_SERVER;
    }

    public VoiceCdr getVoiceCdr() {
        return voiceCdr;
    }

    public void setVoiceCdr(VoiceCdr voiceCdr) {
        this.voiceCdr = voiceCdr;
    }

    public String getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(String callCenterId) {
        this.callCenterId = callCenterId;
    }
}
