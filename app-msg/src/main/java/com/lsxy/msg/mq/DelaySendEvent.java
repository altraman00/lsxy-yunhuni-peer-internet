package com.lsxy.msg.mq;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.mq.api.AbstractDelayMQEvent;

/**
 * Created by zhangxb on 2017/2/10.
 */
public class DelaySendEvent extends AbstractDelayMQEvent {
    private String key;
    private String userId;
    private String userName;
    private String mobiles;
    private String taskName;
    private String msg;
    private String tempId;
    private String tempArgs;
    private String sendType;
    private String operator;
    private String sendTime;
    @Override
    public String getTopicName() {
        return SystemConfig.getProperty("global.mq.topic.app.ussd.api","test_topic_app_ussd_api");
    }

    public DelaySendEvent(Long delay, String key, String userId, String userName, String mobiles, String taskName, String msg, String tempId, String tempArgs, String sendType, String operator, String sendTime) {
        super(delay.intValue());
        this.key = key;
        this.userId = userId;
        this.userName = userName;
        this.mobiles = mobiles;
        this.taskName = taskName;
        this.msg = msg;
        this.tempId = tempId;
        this.tempArgs = tempArgs;
        this.sendType = sendType;
        this.operator = operator;
        this.sendTime = sendTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
