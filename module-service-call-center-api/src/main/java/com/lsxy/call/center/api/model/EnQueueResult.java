package com.lsxy.call.center.api.model;

/**
 * 排队查询可用坐席列表
 * Created by zhangxb on 2016/10/21.
 */
public class EnQueueResult{

    private AppExtension extension;
    private CallCenterAgent agent;

    public CallCenterAgent getAgent() {
        return agent;
    }

    public void setAgent(CallCenterAgent agent) {
        this.agent = agent;
    }

    public AppExtension getExtension() {
        return extension;
    }

    public void setExtension(AppExtension extension) {
        this.extension = extension;
    }
}

