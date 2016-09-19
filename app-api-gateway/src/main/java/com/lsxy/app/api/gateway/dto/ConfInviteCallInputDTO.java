package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by liuws on 2016/8/24.
 */
public class ConfInviteCallInputDTO extends CommonDTO{

    @JsonProperty("from")
    @Size(max = 128)
    private String from;//主叫号码

    @JsonProperty("to")
    @NotNull
    @Size(min = 1, max = 128)
    private String to;//被叫号码

    @JsonProperty("max_duration")
    @NotNull
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer maxDuration;//最大会议时间（秒）

    @JsonProperty("max_dial_duration")
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer maxDialDuration;//最大拨号等待时间（秒）

    @JsonProperty("dial_voice_stop_cond")
    @Min(0)
    @Max(1)
    private Integer dialVoiceStopCond;//自定义拨号音停止播放条件。0：振铃停止；1：接听或者挂断停止。

    @JsonProperty("play_file")
    @Size(max = 128)
    private String playFile;//加入后在会议播放这个文件

    @JsonProperty("voice_mode")
    @Min(1)
    @Max(4)
    private Integer voiceMode;//加入后的声音模式。

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMaxDialDuration() {
        return maxDialDuration;
    }

    public void setMaxDialDuration(Integer maxDialDuration) {
        this.maxDialDuration = maxDialDuration;
    }

    public Integer getDialVoiceStopCond() {
        return dialVoiceStopCond;
    }

    public void setDialVoiceStopCond(Integer dialVoiceStopCond) {
        this.dialVoiceStopCond = dialVoiceStopCond;
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
