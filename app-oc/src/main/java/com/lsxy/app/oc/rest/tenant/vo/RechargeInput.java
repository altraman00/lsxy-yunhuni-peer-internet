package com.lsxy.app.oc.rest.tenant.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by liuws on 2016/8/11.
 */
public class RechargeInput implements Serializable{

    private String tenantId;

    private BigDecimal amount;

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
}
