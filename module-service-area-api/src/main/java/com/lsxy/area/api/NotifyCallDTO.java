package com.lsxy.area.api;

import java.io.Serializable;
import java.util.List;

/**
 * 通知外呼
 * Created by liups on 2016/8/26.
 */
public class NotifyCallDTO implements Serializable {
    private String from;        //主叫号码
    private String to;          //被叫号码
    private List<String> files;     //通知放音文件(列表)
    private Integer repeat;     //重复播放次数
    private Integer max_dial_duration;  //最大拨号等待时间（秒）
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

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Integer getMax_dial_duration() {
        return max_dial_duration;
    }

    public void setMax_dial_duration(Integer max_dial_duration) {
        this.max_dial_duration = max_dial_duration;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }
}
