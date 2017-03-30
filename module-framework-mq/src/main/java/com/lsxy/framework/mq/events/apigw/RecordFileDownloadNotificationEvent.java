package com.lsxy.framework.mq.events.apigw;

import com.lsxy.framework.mq.api.AbstractDelayMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;


/**
 * Created by liups on 2017/3/20.
 */
public class RecordFileDownloadNotificationEvent extends AbstractDelayMQEvent {
    private String appId;
    private String subaccountId;
    private String recordFileId;
    private int count;

    public RecordFileDownloadNotificationEvent() {
    }

    public RecordFileDownloadNotificationEvent(String appId, String subaccountId, String recordFileId, int count) {
        //TODO 延时时间设置
        super(3*60*1000);
        this.appId = appId;
        this.subaccountId = subaccountId;
        this.recordFileId = recordFileId;
        this.count = count;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AREA_SERVER;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getRecordFileId() {
        return recordFileId;
    }

    public void setRecordFileId(String recordFileId) {
        this.recordFileId = recordFileId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
