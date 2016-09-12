package com.lsxy.framework.api.events;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * 删除放音文件事件
 * Created by zhangxb on 2016/9/12.
 */
public class VoiceFilePlayDeleteEvent extends AbstractMQEvent {
    public static final String FILE = "file";
    public static final String APP = "app";
    private String tenantId;
    private String appId;
    private String key;
    private String name;
    private String type;
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_FRAMEWORK_ACCOUNT;
    }
    public VoiceFilePlayDeleteEvent(String type,String tenantId,String appId){
        this.tenantId = tenantId;
        this.appId = appId;
        this.type = type;
    }
    public VoiceFilePlayDeleteEvent(String type,String tenantId,String appId,String key,String name) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.type = type;
        this.key = key;
        this.name = name;
    }

    public VoiceFilePlayDeleteEvent() {
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

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
}
