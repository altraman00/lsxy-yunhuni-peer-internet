package com.lsxy.app.portal.console.home;

/**
 * Created by liups on 2016/6/27.
 */
public class AppStateVO {
    private String id;               //appId

    private String name;             //应用名称

    private Integer status;             //应用上线状态

    private Integer callOfHour;         //1小时内呼叫量

    private Integer callOfDay;          //1天内呼叫量

    private Integer currentCall;   //当前呼叫并发

    private String ivr;             //ivr号吗

    private Boolean ivrExpire;        //ivr号码过期时间

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getIvr() {
        return ivr;
    }

    public void setIvr(String ivr) {
        this.ivr = ivr;
    }

    public Boolean getIvrExpire() {
        return ivrExpire;
    }

    public void setIvrExpire(Boolean ivrExpire) {
        this.ivrExpire = ivrExpire;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
