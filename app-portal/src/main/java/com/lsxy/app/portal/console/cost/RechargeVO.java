package com.lsxy.app.portal.console.cost;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值页面展示VO
 * Created by liups on 2016/7/5.
 */
public class RechargeVO {
    private String tenantId;                //所属租户
    private BigDecimal amount;                //充值金额
    private String type;                  //充值方式
    private String status;                //充值状态
    private String orderId;               //订单ID
    private Date createTime;

    private String typeName;                //页面显示的type的中文
    private String statusName;              //页面显示的status的中文

    public RechargeVO(){

    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

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
