package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 月结账单
 * Created by liups on 2016/7/11.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_bill_month")
public class BillMonth extends IdEntity {
    private Date dt;                    //统计时间
    private Date month;                 //月份
    private Double sumAmount;           //总额
    private Double singleWayCall;       //单向外呼
    private Double bothWayCall;         //双向外呼
    private Double callConference;      //电话会议
    private Double ivrCall;             //电话接入IVR
    private Double ivrRevoice;          //IVR外呼放音
    private Double sms;                 //短信
    private Double callNotice;          //电话通知
    private Double callRecord;          //通话录音
    private String appId;               //所属应用
    private String tenantId;            //所属租户

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public Double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Double sumAmount) {
        this.sumAmount = sumAmount;
    }

    public Double getSingleWayCall() {
        return singleWayCall;
    }

    public void setSingleWayCall(Double singleWayCall) {
        this.singleWayCall = singleWayCall;
    }

    public Double getBothWayCall() {
        return bothWayCall;
    }

    public void setBothWayCall(Double bothWayCall) {
        this.bothWayCall = bothWayCall;
    }

    public Double getCallConference() {
        return callConference;
    }

    public void setCallConference(Double callConference) {
        this.callConference = callConference;
    }

    public Double getIvrCall() {
        return ivrCall;
    }

    public void setIvrCall(Double ivrCall) {
        this.ivrCall = ivrCall;
    }

    public Double getIvrRevoice() {
        return ivrRevoice;
    }

    public void setIvrRevoice(Double ivrRevoice) {
        this.ivrRevoice = ivrRevoice;
    }

    public Double getSms() {
        return sms;
    }

    public void setSms(Double sms) {
        this.sms = sms;
    }

    public Double getCallNotice() {
        return callNotice;
    }

    public void setCallNotice(Double callNotice) {
        this.callNotice = callNotice;
    }

    public Double getCallRecord() {
        return callRecord;
    }

    public void setCallRecord(Double callRecord) {
        this.callRecord = callRecord;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
