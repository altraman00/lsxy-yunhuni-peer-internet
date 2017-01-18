package com.lsxy.app.oc.rest.tenant.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by liuws on 2016/8/11.
 */
public class RechargeInput implements Serializable{
    @ApiModelProperty(name="amount",value = "金额")
    private BigDecimal amount;
    @ApiModelProperty(name="source",value = "用户充值:USER;手工_活动赠送:MANUAL_ACTIVITY;手工_线下商务:MANUAL_BUSINESS;手工_其他:MANUAL_OTHER;手工_测试:MANUAL_TEST")
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
