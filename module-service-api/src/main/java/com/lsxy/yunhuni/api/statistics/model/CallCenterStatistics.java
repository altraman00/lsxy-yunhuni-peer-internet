package com.lsxy.yunhuni.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 呼叫中心总数统计
 * Created by liups on 2016/11/7.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center_statistics")
public class CallCenterStatistics extends IdEntity {
    private String tenantId;
    private String appId;
    private Date dt;
    private Long callIn; //呼入量
    private Long callInSuccess; //呼入成功量
    private Long callOut; //呼出量
    private Long callOutSuccess; //呼出成功量
    private Long toManualSuccess; //转接成功量
    private Long queueNum; //排队次数
    private Long queueDuration; //排队时长
    private Long callTimeLong;

    public CallCenterStatistics() {
    }

    public CallCenterStatistics(String tenantId, String appId, Date dt, Long callIn, Long callInSuccess, Long callOut, Long callOutSuccess,
                                Long toManualSuccess, Long queueNum, Long queueDuration,Long callTimeLong) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.dt = dt;
        this.callIn = callIn;
        this.callInSuccess = callInSuccess;
        this.callOut = callOut;
        this.callOutSuccess = callOutSuccess;
        this.toManualSuccess = toManualSuccess;
        this.queueNum = queueNum;
        this.queueDuration = queueDuration;
        this.callTimeLong = callTimeLong;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "call_in")
    public Long getCallIn() {
        return callIn;
    }

    public void setCallIn(Long callIn) {
        this.callIn = callIn;
    }

    @Column(name = "call_in_success")
    public Long getCallInSuccess() {
        return callInSuccess;
    }

    public void setCallInSuccess(Long callInSuccess) {
        this.callInSuccess = callInSuccess;
    }

    @Column(name = "call_out")
    public Long getCallOut() {
        return callOut;
    }

    public void setCallOut(Long callOut) {
        this.callOut = callOut;
    }

    @Column(name = "call_out_success")
    public Long getCallOutSuccess() {
        return callOutSuccess;
    }

    public void setCallOutSuccess(Long callOutSuccess) {
        this.callOutSuccess = callOutSuccess;
    }

    @Column(name = "to_manual_success")
    public Long getToManualSuccess() {
        return toManualSuccess;
    }

    public void setToManualSuccess(Long toManualSuccess) {
        this.toManualSuccess = toManualSuccess;
    }

    @Column(name = "queue_num")
    public Long getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(Long queueNum) {
        this.queueNum = queueNum;
    }

    @Column(name = "queue_duration")
    public Long getQueueDuration() {
        return queueDuration;
    }

    public void setQueueDuration(Long queueDuration) {
        this.queueDuration = queueDuration;
    }

    @Column(name = "call_time_long")
    public Long getCallTimeLong() {
        return callTimeLong;
    }

    public void setCallTimeLong(Long callTimeLong) {
        this.callTimeLong = callTimeLong;
    }

    public static class Builder {
        private String tenantId;
        private String appId;
        private Date dt;
        private Long callIn; //呼入量
        private Long callInSuccess; //呼入成功量
        private Long callOut; //呼出量
        private Long callOutSuccess; //呼出成功量
        private Long toManualSuccess; //转接成功量
        private Long queueNum; //排队次数
        private Long queueDuration; //排队时长
        private Long callTimeLong;
        public Builder(String tenantId, String appId, Date dt) {
            this.tenantId = tenantId;
            this.appId = appId;
            this.dt = dt;
        }

        public Builder setCallIn(Long callIn) {
            this.callIn = callIn;
            return this;
        }

        public Builder setCallInSuccess(Long callInSuccess) {
            this.callInSuccess = callInSuccess;
            return this;
        }

        public Builder setCallOut(Long callOut) {
            this.callOut = callOut;
            return this;
        }

        public Builder setCallOutSuccess(Long callOutSuccess) {
            this.callOutSuccess = callOutSuccess;
            return this;
        }

        public Builder setToManualSuccess(Long toManualSuccess) {
            this.toManualSuccess = toManualSuccess;
            return this;
        }

        public Builder setQueueNum(Long queueNum) {
            this.queueNum = queueNum;
            return this;
        }

        public Builder setQueueDuration(Long queueDuration) {
            this.queueDuration = queueDuration;
            return this;
        }

        public Builder setCallTimeLong(Long callTimeLong) {
            this.callTimeLong = callTimeLong;
            return this;
        }
        public CallCenterStatistics build(){
            return new CallCenterStatistics(tenantId,appId,dt,callIn,callInSuccess,callOut,callOutSuccess,toManualSuccess,queueNum,queueDuration,callTimeLong);
        }
    }
}
