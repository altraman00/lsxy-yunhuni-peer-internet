package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 双向回拔
 * Created by liups on 2016/8/23.
 */
public class DuoCallbackDTO implements Serializable {
    private String from1;               //第一方主叫号码
    @NotNull
    private String to1;                 //第一方被叫号码
    private String from2;               //第二方主叫号码
    @NotNull
    private String to2;                 //第二方被叫号码
    private String ring_tone;           //自定义回铃音
    @Min(0)
    @Max(3)
    private Integer ring_tone_mode = 0;     //自定义回铃音播放模式0：收到对端回铃后开始播放 1：拨号时即开始播放，收到对端回铃后停止播放 2：拨号时即开始播放，对端接听或者挂机后停止播放
    @Min(1)
    @Max(5 * 60)
    private Integer max_dial_duration = 45;  //最大拨号等待时间（秒）
    @NotNull
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer max_call_duration;  // 最大接通时间（秒）
    private Boolean recording = false ;         //是否录音
    @Min(0)
    @Max(3)
    private Integer record_mode = 0;        // 录音模式0: 双向接通后录音  1：开始呼叫第一方时启动录音  2: 开始呼叫第二方时启动录音
//    private Integer countdown_time ;       //倒计时时间点
//    private String countdown_voice;       //倒计时播放语音文件
//    private String callback_url;        //结果通知 HTTP 地址
//    private String callback_method = "POST";     //结果通知 HTTP 方法, GET(默认) or POST
    @Size(max = 128)
    private String user_data;           //用户数据

    public String getFrom1() {
        return from1;
    }

    public void setFrom1(String from1) {
        this.from1 = from1;
    }

    public String getTo1() {
        return to1;
    }

    public void setTo1(String to1) {
        this.to1 = to1;
    }

    public String getFrom2() {
        return from2;
    }

    public void setFrom2(String from2) {
        this.from2 = from2;
    }

    public String getTo2() {
        return to2;
    }

    public void setTo2(String to2) {
        this.to2 = to2;
    }

    public String getRing_tone() {
        return ring_tone;
    }

    public void setRing_tone(String ring_tone) {
        this.ring_tone = ring_tone;
    }

    public Integer getRing_tone_mode() {
        return ring_tone_mode;
    }

    public void setRing_tone_mode(Integer ring_tone_mode) {
        this.ring_tone_mode = ring_tone_mode;
    }

    public Integer getMax_dial_duration() {
        return max_dial_duration;
    }

    public void setMax_dial_duration(Integer max_dial_duration) {
        this.max_dial_duration = max_dial_duration;
    }

    public Integer getMax_call_duration() {
        return max_call_duration;
    }

    public void setMax_call_duration(Integer max_call_duration) {
        this.max_call_duration = max_call_duration;
    }

    public Boolean getRecording() {
        return recording;
    }

    public void setRecording(Boolean recording) {
        this.recording = recording;
    }

    public Integer getRecord_mode() {
        return record_mode;
    }

    public void setRecord_mode(Integer record_mode) {
        this.record_mode = record_mode;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }
}
