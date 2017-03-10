package com.lsxy.msg.mq;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.mq.api.AbstractDelayMQEvent;

/**
 * Created by zhangxb on 2017/2/10.
 */
public class DelaySendMassEvent extends AbstractDelayMQEvent {
    private String key;
    private String taskName;
    private String tempId;
    private String tempArgs;
    private String mobiles;
    private String sendTime;
    private String msg;
    private String sendType;
    private String operator;

    @Override
    public String getTopicName() {
        return SystemConfig.getProperty("global.mq.topic.app.ussd.api","test_topic_app_ussd_api");
    }

    public DelaySendMassEvent(Long delay, String key, String taskName, String tempId, String tempArgs, String mobiles, String sendTime, String msg, String sendType, String operator) {
        super(delay.intValue());
        this.key = key;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
}
