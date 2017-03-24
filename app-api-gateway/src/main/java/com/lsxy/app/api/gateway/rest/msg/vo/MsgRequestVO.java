package com.lsxy.app.api.gateway.rest.msg.vo;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.msg.api.model.MsgUserRequest;

/**
 * Created by liups on 2017/3/20.
 */
public class MsgRequestVO {
    private String msgKey;
    private String taskName;
    private String tempId;
    private String tempArgs;
    private String sendTime;
    private String sendType;
    private Boolean isMass;
    private Long sumNum;
    private Integer state;
    private Long succNum;
    private Long failNum;
    private Long pendingNum;
    private String mobile;

    public MsgRequestVO() {
    }

    public MsgRequestVO(MsgUserRequest request) {
        this.msgKey = request.getMsgKey();
        this.taskName = request.getTaskName();
        this.tempId = request.getTempId();
        this.tempArgs = request.getTempArgs();
        this.sendTime = request.getSendTime() == null?null: DateUtils.getDate(request.getSendTime(),"yyyy-MM-dd HH:mm:ss");
        this.sendType = request.getSendType();
        this.isMass = request.getIsMass();
        this.sumNum = request.getSumNum();
        this.state = request.getState();
        this.succNum = request.getSuccNum();
        this.failNum = request.getFailNum();
        this.pendingNum = request.getPendingNum();
        this.mobile = request.getMobile();
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public Boolean getMass() {
        return isMass;
    }

    public void setMass(Boolean mass) {
        isMass = mass;
    }

    public Long getSumNum() {
        return sumNum;
    }

    public void setSumNum(Long sumNum) {
        this.sumNum = sumNum;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getSuccNum() {
        return succNum;
    }

    public void setSuccNum(Long succNum) {
        this.succNum = succNum;
    }

    public Long getFailNum() {
        return failNum;
    }

    public void setFailNum(Long failNum) {
        this.failNum = failNum;
    }

    public Long getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(Long pendingNum) {
        this.pendingNum = pendingNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
