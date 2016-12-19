package com.lsxy.yunhuni.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/10/21.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_day_statics")
public class DayStatics extends IdEntity{
    private String tenantId;
    private String appId;
    private Date dt;
    private BigDecimal recharge;
    private BigDecimal consume;
    private Long callConnect;
    private Long callSum;
    private Long callCostTime;

    public DayStatics() {
    }

    public DayStatics(String tenantId, String appId, Date dt, BigDecimal recharge, BigDecimal consume, Long callConnect, Long callSum, Long callCostTime) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.dt = dt;
        this.recharge = recharge;
        this.consume = consume;
        this.callConnect = callConnect;
        this.callSum = callSum;
        this.callCostTime = callCostTime;
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

    @Column(name = "recharge")
    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    @Column(name = "consume")
    public BigDecimal getConsume() {
        return consume;
    }

    public void setConsume(BigDecimal consume) {
        this.consume = consume;
    }

    @Column(name = "call_connect")
    public Long getCallConnect() {
        return callConnect;
    }

    public void setCallConnect(Long callConnect) {
        this.callConnect = callConnect;
    }

    @Column(name = "call_sum")
    public Long getCallSum() {
        return callSum;
    }

    public void setCallSum(Long callSum) {
        this.callSum = callSum;
    }

    @Column(name = "call_cost_time")
    public Long getCallCostTime() {
        return callCostTime;
    }

    public void setCallCostTime(Long callCostTime) {
        this.callCostTime = callCostTime;
    }
}
