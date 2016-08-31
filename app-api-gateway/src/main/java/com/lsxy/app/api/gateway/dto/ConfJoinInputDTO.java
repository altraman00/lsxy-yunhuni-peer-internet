package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/24.
 */
public class ConfJoinInputDTO implements Serializable{

    @JsonProperty("call_id")
    private String callId;//呼叫id

    @JsonProperty("max_duration")
    private Integer maxDuration;//最大会议时间（秒）

    @JsonProperty("play_file")
    private String playFile;//加入后在会议播放这个文件

    @JsonProperty("voice_mode")
    private Integer voiceMode;//加入后的声音模式。

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
