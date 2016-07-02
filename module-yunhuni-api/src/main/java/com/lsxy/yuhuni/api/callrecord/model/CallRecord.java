package com.lsxy.yuhuni.api.callrecord.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yuhuni.api.app.model.App;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_record")
public class CallRecord extends IdEntity {
    private String areaId;      //所属区域ID
    private Tenant tenant;      //所属租户
    private App app;            //所属应用
    private String lineId;      //线路ID
    private String callId;      //呼叫号
    private String dir;         //呼叫方向
    private String from01;      //主叫
    private String to01;        //被叫
    private String from02;      //主叫02
    private String to02;        //被叫02
    private Date callStartTime; //呼叫开始时间
    private Date callAckTime;   //呼叫应答时间
    private Date callEndTime;   //呼叫结束时间
    private double cost;        //成本价

    @Column(name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Column(name = "line_id")
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Column(name = "call_id")
    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    @Column(name = "dir")
    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Column(name = "from01")
    public String getFrom01() {
        return from01;
    }

    public void setFrom01(String from01) {
        this.from01 = from01;
    }

    @Column(name = "to01")
    public String getTo01() {
        return to01;
    }

    public void setTo01(String to01) {
        this.to01 = to01;
    }

    @Column(name = "from02")
    public String getFrom02() {
        return from02;
    }

    public void setFrom02(String from02) {
        this.from02 = from02;
    }

    @Column(name = "to02")
    public String getTo02() {
        return to02;
    }

    public void setTo02(String to02) {
        this.to02 = to02;
    }

    @Column(name = "call_start_dt")
    public Date getCallStartTime() {
        return callStartTime;
    }

    public void setCallStartTime(Date callStartTime) {
        this.callStartTime = callStartTime;
    }

    @Column(name = "call_ack_dt")
    public Date getCallAckTime() {
        return callAckTime;
    }

    public void setCallAckTime(Date callAckTime) {
        this.callAckTime = callAckTime;
    }

    @Column(name = "call_end_dt")
    public Date getCallEndTime() {
        return callEndTime;
    }

    public void setCallEndTime(Date callEndTime) {
        this.callEndTime = callEndTime;
    }

    @Column(name = "cost")
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
