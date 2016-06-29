package com.lsxy.app.portal.rest.console.home.vo;

/**
 * Created by liups on 2016/6/27.
 */
public class AppStateVO {
    private String appId;               //appId

    private String name;             //应用名称

    private Integer status;             //应用上线状态

    private Integer callOfHour;         //1小时内呼叫量

    private Integer callOfDay;          //1天内呼叫量

    private Integer currentCall;   //当前呼叫并发

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCallOfHour() {
        return callOfHour;
    }

    public void setCallOfHour(Integer callOfHour) {
        this.callOfHour = callOfHour;
    }

    public Integer getCallOfDay() {
        return callOfDay;
    }

    public void setCallOfDay(Integer callOfDay) {
        this.callOfDay = callOfDay;
    }

    public Integer getCurrentCall() {
        return currentCall;
    }

    public void setCurrentCall(Integer currentCall) {
        this.currentCall = currentCall;
    }
}
