package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
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
    private Integer month;                 //月份
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

    public BillMonth(){

    }

    public BillMonth(String tenantId,Date dt,Double singleWayCall,Double bothWayCall,Double callConference,Double ivrCall,
                     Double ivrRevoice,Double sms,Double callNotice,Double callRecord){
        this.tenantId = tenantId;
        this.dt = dt;
        this.singleWayCall = singleWayCall;
        this.bothWayCall = bothWayCall;
        this.callConference = callConference;
        this.ivrCall = ivrCall;
        this.ivrRevoice = ivrRevoice;
        this.sms = sms;
        this.callNotice = callNotice;
        this.callRecord = callRecord;
    }

    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "month")
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getSumAmount() {
        //合计
        return (singleWayCall==null?0D:singleWayCall) +
                (bothWayCall==null?0D:bothWayCall) +
                (callConference==null?0D:callConference) +
                (ivrCall==null?0D:ivrCall) +
                (ivrRevoice ==null?0D:ivrRevoice)+
                (sms==null?0D:sms) +
                (callNotice==null?0D:callNotice) +
                (callRecord==null?0D:callRecord);
    }

    public void setSumAmount(Double sumAmount) {
        this.sumAmount = sumAmount;
    }

    @Column(name = "single_way_call")
    public Double getSingleWayCall() {
        return singleWayCall;
    }

    public void setSingleWayCall(Double singleWayCall) {
        this.singleWayCall = singleWayCall;
    }

    @Column(name = "both_way_call")
    public Double getBothWayCall() {
        return bothWayCall;
    }

    public void setBothWayCall(Double bothWayCall) {
        this.bothWayCall = bothWayCall;
    }

    @Column(name = "call_conference")
    public Double getCallConference() {
        return callConference;
    }

    public void setCallConference(Double callConference) {
        this.callConference = callConference;
    }

    @Column(name = "IVR_call")
    public Double getIvrCall() {
        return ivrCall;
    }

    public void setIvrCall(Double ivrCall) {
        this.ivrCall = ivrCall;
    }

    @Column(name = "IVR_revoice")
    public Double getIvrRevoice() {
        return ivrRevoice;
    }

    public void setIvrRevoice(Double ivrRevoice) {
        this.ivrRevoice = ivrRevoice;
    }

    @Column(name = "sms")
    public Double getSms() {
        return sms;
    }

    public void setSms(Double sms) {
        this.sms = sms;
    }

    @Column(name = "call_notice")
    public Double getCallNotice() {
        return callNotice;
    }

    public void setCallNotice(Double callNotice) {
        this.callNotice = callNotice;
    }

    @Column(name = "call_record")
    public Double getCallRecord() {
        return callRecord;
    }

    public void setCallRecord(Double callRecord) {
        this.callRecord = callRecord;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
