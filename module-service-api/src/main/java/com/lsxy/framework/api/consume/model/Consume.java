package com.lsxy.framework.api.consume.model;

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
    public static String RENT_NUMBER = "rent_number";  //租用号码的消费类型

    private Date dt;//消费时间
    private String type;//消费类型 或产品的标识码
    private BigDecimal amount;//消费金额
    private String remark;//备注
    private String appId;//所属应用编号（仅用查询，如需关联应用等合并结构后改）
    private Tenant tenant;//所属租户

    public Consume() {
    }

    public Consume(Date dt, String type, BigDecimal amount, String remark, String appId, Tenant tenant) {
        this.dt = dt;
        this.type = type;
        this.amount = amount;
        this.remark = remark;
        this.appId = appId;
        this.tenant = tenant;
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
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
