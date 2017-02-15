package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 加入会议
 * Created by liuws on 2016/8/24.
 */
public class ConfJoinInputDTO extends CommonDTO{

    @JsonProperty("call_id")
    @NotNull
    @Size(min = 1,max = 128)
    private String callId;//呼叫id

    @JsonProperty("max_duration")
    @NotNull
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer maxDuration;//最大会议时间（秒）

    @JsonProperty("play_file")
    @Size(max = 128)
    private String playFile;//加入后在会议播放这个文件

    @JsonProperty("voice_mode")
    @Min(1)
    @Max(4)
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
