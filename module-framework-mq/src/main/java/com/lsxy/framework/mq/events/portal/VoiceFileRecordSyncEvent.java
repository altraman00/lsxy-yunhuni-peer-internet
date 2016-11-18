package com.lsxy.framework.mq.events.portal;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * 录音文件上传同步事件
 * Created by zhangxb on 2016/9/12.
 */
public class VoiceFileRecordSyncEvent extends AbstractMQEvent {
    public static final String TYPE_FILE = "file";
    public static final String TYPE_CDR = "cdr";
    private String tenantId;
    private String appId;
    private String key;
    private String type;
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_APP_PORTAL;
    }
    public VoiceFileRecordSyncEvent( String tenantId, String appId,String key,String type){
        this.tenantId = tenantId;
        this.appId = appId;
        this.key = key;
        this.type=type;
    }

    public VoiceFileRecordSyncEvent() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
