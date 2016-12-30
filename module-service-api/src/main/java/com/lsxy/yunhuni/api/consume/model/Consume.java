package com.lsxy.yunhuni.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费记录表对象
 * Created by zhangxb on 2016/7/8.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_consume")
public class Consume extends IdEntity {

    private Date dt;//消费时间
    private String type;//消费类型 或产品的标识码（不能为空）
    private BigDecimal amount;//消费金额
    private String remark;//备注
    private String appId;//所属应用编号（不能为空，仅用查询，勿关联应用，当一个消费不属于任何应用时，设为0）
    private String tenantId;//所属租户
    private String relevanceId; //消费的相关ID


    public Consume() {
    }

    public Consume(Date dt, String type, BigDecimal amount, String remark, String appId, String tenantId,String relevanceId) {
        this.dt = dt;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.appId = appId;
        this.tenantId = tenantId;
        this.relevanceId = relevanceId;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    @Column(name = "relevance_id")
    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }
}
