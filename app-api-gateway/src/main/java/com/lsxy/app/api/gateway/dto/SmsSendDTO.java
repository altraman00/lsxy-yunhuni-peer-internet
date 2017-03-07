package com.lsxy.app.api.gateway.dto;

/**
 * Created by liups on 2017/3/7.
 */
public class SmsSendDTO extends CommonDTO {
    private String destPhone;
    private String tempId;
    private String tempArgs;

    public String getDestPhone() {
        return destPhone;
    }

    public void setDestPhone(String destPhone) {
        this.destPhone = destPhone;
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
