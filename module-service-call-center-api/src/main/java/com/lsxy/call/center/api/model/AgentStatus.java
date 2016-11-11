package com.lsxy.call.center.api.model;

import java.io.Serializable;

/**
 * 坐席状态(redis)
 * Created by liuws on 2016/11/7.
 */
public class AgentStatus implements Serializable{

    private String extension;

    private String state;

    private long lastRegTime;

    private long lastTime;

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getLastRegTime() {
        return lastRegTime;
    }

    public void setLastRegTime(long lastRegTime) {
        this.lastRegTime = lastRegTime;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
