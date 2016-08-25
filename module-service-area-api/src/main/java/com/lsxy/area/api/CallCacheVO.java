package com.lsxy.area.api;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/25.
 */
public class CallCacheVO implements Serializable{
    private String callId;
    private String callType;
    private String userdata;

    public CallCacheVO() {
    }

    public CallCacheVO(String callId, String callType, String userdata) {
        this.callId = callId;
        this.callType = callType;
        this.userdata = userdata;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getUserdata() {
        return userdata;
    }

    public void setUserdata(String userdata) {
        this.userdata = userdata;
    }
}
