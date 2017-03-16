package com.lsxy.app.api.gateway.rest.msg.dto;

import com.lsxy.app.api.gateway.dto.CommonDTO;

/**
 * Created by liups on 2017/3/9.
 */
public class UssdSendDTO extends CommonDTO {
    private String mobile;
    private String tempId;
    private String tempArgs;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getTempArgs() {
        return tempArgs;
    }

    public void setTempArgs(String tempArgs) {
        this.tempArgs = tempArgs;
    }
}
