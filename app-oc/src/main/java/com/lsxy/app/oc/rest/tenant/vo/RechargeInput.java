package com.lsxy.app.oc.rest.tenant.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by liuws on 2016/8/11.
 */
public class RechargeInput implements Serializable{

    private BigDecimal amount;
    private String source;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
