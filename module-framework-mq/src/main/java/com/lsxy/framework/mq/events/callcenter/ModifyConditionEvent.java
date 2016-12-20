package com.lsxy.framework.mq.events.callcenter;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/11/9.
 */
public class ModifyConditionEvent extends AbstractMQEvent {

    private String conditionId;

    private String tenantId;

    private String appId;

    private boolean mWhere;

    private boolean mSort;

    private boolean mPriority;

    public ModifyConditionEvent(){
    }

    public ModifyConditionEvent(String conditionId, String tenantId, String appId,boolean mWhere,boolean mSort,boolean mPriority){
        this.conditionId = conditionId;
        this.tenantId = tenantId;
        this.appId = appId;
        this.mWhere = mWhere;
        this.mSort = mSort;
        this.mPriority = mPriority;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_CALL_CENTER;
    }

    public String getConditionId() {
        return conditionId;
    }

    public void setConditionId(String conditionId) {
        this.conditionId = conditionId;
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

    public boolean ismWhere() {
        return mWhere;
    }

    public void setmWhere(boolean mWhere) {
        this.mWhere = mWhere;
    }

    public boolean ismSort() {
        return mSort;
    }

    public void setmSort(boolean mSort) {
        this.mSort = mSort;
    }

    public boolean ismPriority() {
        return mPriority;
    }

    public void setmPriority(boolean mPriority) {
        this.mPriority = mPriority;
    }
}
