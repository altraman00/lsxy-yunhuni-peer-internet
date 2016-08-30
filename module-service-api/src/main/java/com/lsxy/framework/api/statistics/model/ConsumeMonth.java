package com.lsxy.framework.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费月统计
 * Created by zhangxb on 2016/7/6.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_consume_month")
public class ConsumeMonth extends IdEntity {
    private Date dt;//统计时间
    private Integer month;//统计月 区间1-12
    private BigDecimal amongAmount;//本月消费统计 decimal(20,4) DEFAULT NULL,
    private String appId;//所属应用,
    private String tenantId;//所属租户
    private String type;//消费类型
    @Column(name = "type")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "month")
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
    @Column(name = "among_amount")
    public BigDecimal getAmongAmount() {
        return amongAmount;
    }

    public void setAmongAmount(BigDecimal amongAmount) {
        this.amongAmount = amongAmount;
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
}
