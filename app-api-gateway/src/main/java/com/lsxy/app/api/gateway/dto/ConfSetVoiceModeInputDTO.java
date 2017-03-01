package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 设置会议成员放音模式
 * Created by liuws on 2016/8/24.
 */
public class ConfSetVoiceModeInputDTO extends CommonDTO{

    @JsonProperty("call_id")
    @NotNull
    @Size(min = 1,max = 128)
    private String callId;

    @JsonProperty("voice_mode")
    @NotNull
    @Min(1)
    @Max(4)
    private Integer voiceMode;

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public Integer getVoiceMode() {
        return voiceMode;
    }

    public void setVoiceMode(Integer voiceMode) {
        this.voiceMode = voiceMode;
    }
}
