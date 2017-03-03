package com.lsxy.framework.mq.events.agentserver;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/11/9.
 */
public class RecordCompletedEvent extends AbstractMQEvent {

    private String recordId;

    private String tenantId;

    private String appId;

    private String callCenterId;

    private String subaccountId;

    private String areaId;

    private String callId;

    private String type;

    private String url;

    private Long starTime;

    private Long endTime;

    public RecordCompletedEvent(){
    }

    public RecordCompletedEvent(String recordId,String callCenterId,String tenantId, String appId,String subaccountId,String areaId,String callId,
                                String type,String url,Long startTime,Long endTime){
        this(callCenterId,tenantId, appId,subaccountId, areaId, callId, type, url, startTime, endTime);
        this.recordId = recordId;
    }
    public RecordCompletedEvent(String callCenterId,String tenantId, String appId,String subaccountId,String areaId,String callId,
                                String type,String url,Long startTime,Long endTime){
        this.tenantId = tenantId;
        this.appId = appId;
        this.callCenterId = callCenterId;
        this.subaccountId = subaccountId;
        this.areaId = areaId;
        this.callId = callId;
        this.type = type;
        this.url = url;
        this.starTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_AREA_SERVER;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
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

    public String getCallCenterId() {
        return callCenterId;
    }

    public void setCallCenterId(String callCenterId) {
        this.callCenterId = callCenterId;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getStarTime() {
        return starTime;
    }

    public void setStarTime(Long starTime) {
        this.starTime = starTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
