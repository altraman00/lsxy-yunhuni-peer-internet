package com.lsxy.app.api.gateway.rest.msg.dto;

import com.lsxy.app.api.gateway.dto.CommonDTO;

/**
 * Created by liups on 2017/3/9.
 */
public class SmsSendMassDTO extends CommonDTO {
    private String taskName;
    private String tempId;
    private String tempArgs;
    private String mobiles;
    private String sendTime;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
