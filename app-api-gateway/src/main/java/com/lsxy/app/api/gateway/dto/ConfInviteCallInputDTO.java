package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/24.
 */
public class ConfInviteCallInputDTO implements Serializable{

    @JsonProperty("from")
    private String from;//主叫号码

    @JsonProperty("to")
    private String to;//被叫号码

    @JsonProperty("custom_from")
    private String customFrom;//自定义主叫号码

    @JsonProperty("custom_to")
    private String customTo;//自定义被叫号码

    @JsonProperty("max_duration")
    private Integer maxDuration;//最大会议时间（秒）

    @JsonProperty("max_dial_duration")
    private Integer maxDialDuration;//最大拨号等待时间（秒）

    @JsonProperty("dial_voice_stop_cond")
    private Integer dialVoiceStopCond;//自定义拨号音停止播放条件。0：振铃停止；1：接听或者挂断停止。

    @JsonProperty("play_file")
    private String playFile;//加入后在会议播放这个文件

    @JsonProperty("voice_mode")
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

    public String getCustomFrom() {
        return customFrom;
    }

    public void setCustomFrom(String customFrom) {
        this.customFrom = customFrom;
    }

    public String getCustomTo() {
        return customTo;
    }

    public void setCustomTo(String customTo) {
        this.customTo = customTo;
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
