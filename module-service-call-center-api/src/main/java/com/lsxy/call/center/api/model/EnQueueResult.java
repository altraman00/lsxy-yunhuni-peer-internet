package com.lsxy.call.center.api.model;

/**
 * 排队查询可用坐席列表
 * Created by zhangxb on 2016/10/21.
 */
public class EnQueueResult{

    private String id;
    private String type;
    private String user;
    private String telenum;
    private String agent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTelenum() {
        return telenum;
    }

    public void setTelenum(String telenum) {
        this.telenum = telenum;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}

