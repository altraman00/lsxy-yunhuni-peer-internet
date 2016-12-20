package com.lsxy.framework.mq.events.agentserver;

import com.lsxy.framework.mq.api.AbstractDelayMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/9/13.
 */
public class EnterConversationEvent extends AbstractDelayMQEvent{

    private String conversationId;
    private String callId;
    private Integer maxDuration;
    private String playFile;
    private Integer voiceMode;

    public EnterConversationEvent(){}

    public EnterConversationEvent(String callId,String conversationId,
                                  Integer maxDuration, String playFile, Integer voiceMode){
        //延时50毫秒
        super(50);
        this.callId = callId;
        this.conversationId = conversationId;
        this.maxDuration = maxDuration;
        this.playFile = playFile;
        this.voiceMode = voiceMode;
    }
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AREA_SERVER;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public String getPlayFile() {
        return playFile;
    }

    public void setPlayFile(String playFile) {
        this.playFile = playFile;
    }

    public Integer getVoiceMode() {
        return voiceMode;
    }

    public void setVoiceMode(Integer voiceMode) {
        this.voiceMode = voiceMode;
    }
}
