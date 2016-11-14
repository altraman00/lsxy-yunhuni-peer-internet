package com.lsxy.call.center.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by liups on 2016/11/14.
 */
public class AppExtensionStatus {
    public static String EXTENSION_STATUS_PREFIX = "EXTENSION_STATUS_";

//    @JsonProperty("register_expires")
    private Integer registerExpires;

//    @JsonProperty("last_register_time")
    private Date lastRegisterTime;

//    @JsonProperty("last_register_status")
    private Integer lastRegisterStatus;

//    @JsonProperty("last_action")
    private Integer lastAction;

//    @JsonProperty("last_action")
    private Date lastActionTime;

    private String bindingAgent;


    public Integer getRegisterExpires() {
        return registerExpires;
    }

    public void setRegisterExpires(Integer registerExpires) {
        this.registerExpires = registerExpires;
    }

    public Date getLastRegisterTime() {
        return lastRegisterTime;
    }

    public void setLastRegisterTime(Date lastRegisterTime) {
        this.lastRegisterTime = lastRegisterTime;
    }

    public Integer getLastRegisterStatus() {
        return lastRegisterStatus;
    }

    public void setLastRegisterStatus(Integer lastRegisterStatus) {
        this.lastRegisterStatus = lastRegisterStatus;
    }

    public Integer getLastAction() {
        return lastAction;
    }

    public void setLastAction(Integer lastAction) {
        this.lastAction = lastAction;
    }

    public Date getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(Date lastActionTime) {
        this.lastActionTime = lastActionTime;
    }

    public String getBindingAgent() {
        return bindingAgent;
    }

    public void setBindingAgent(String bindingAgent) {
        this.bindingAgent = bindingAgent;
    }
}
