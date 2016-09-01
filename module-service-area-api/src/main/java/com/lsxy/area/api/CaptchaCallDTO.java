package com.lsxy.area.api;

import java.io.Serializable;

/**
 * 语音验证码
 * Created by liups on 2016/9/1.
 */
public class CaptchaCallDTO implements Serializable {
    private String from;
    private String to;
    private String custom_from;
    private String custom_to;
    private String max_call_duration;
    private String max_dial_duration;
    private String files;
    private String user_data;

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

    public String getCustom_from() {
        return custom_from;
    }

    public void setCustom_from(String custom_from) {
        this.custom_from = custom_from;
    }

    public String getCustom_to() {
        return custom_to;
    }

    public void setCustom_to(String custom_to) {
        this.custom_to = custom_to;
    }

    public String getMax_call_duration() {
        return max_call_duration;
    }

    public void setMax_call_duration(String max_call_duration) {
        this.max_call_duration = max_call_duration;
    }

    public String getMax_dial_duration() {
        return max_dial_duration;
    }

    public void setMax_dial_duration(String max_dial_duration) {
        this.max_dial_duration = max_dial_duration;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }
}
