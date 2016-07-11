package com.lsxy.app.portal.console.cost;

import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.recharge.model.Recharge;

import java.util.Date;

/**
 * 充值页面展示VO
 * Created by liups on 2016/7/5.
 */
public class RechargeVO extends Recharge{
    private Tenant tenant;                //所属租户
    private Double amount;                //充值金额
    private String type;                  //充值方式
    private String status;                //充值状态
    private String orderId;               //订单ID
    private Date createTime;

    private String typeName;                //页面显示的type的中文
    private String statusName;              //页面显示的status的中文

    public RechargeVO(){

    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public Double getAmount() {
        return amount;
    }

    @Override
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getOrderId() {
        return orderId;
    }

    @Override
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
