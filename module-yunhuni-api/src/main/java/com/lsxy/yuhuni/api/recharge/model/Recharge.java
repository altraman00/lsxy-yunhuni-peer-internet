package com.lsxy.yuhuni.api.recharge.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.yuhuni.api.recharge.enums.RechargeStatus;
import com.lsxy.yuhuni.api.recharge.enums.RechargeType;
import com.lsxy.framework.api.tenant.model.Tenant;

import javax.persistence.*;

/**
 * 充值记录表
 * Created by liups on 2016/7/1.
 */
@Entity
@Table(schema = "db_lsxy_base", name = "tb_base_recharge")
public class Recharge  extends IdEntity {

    private Tenant tenant;                //所属租户
    private Double amount;                //充值金额
    private String type;                  //充值方式
    private String status;                //充值状态
    private String orderId;               //订单ID

    public Recharge() {
    }

    public Recharge(Tenant tenant, Double amount, RechargeType type, RechargeStatus status, String orderId) {
        this.tenant = tenant;
        this.amount = amount;
        this.type = type.getName();
        this.status = status.getName();
        this.orderId = orderId;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "order_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
