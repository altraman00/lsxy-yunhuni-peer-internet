package com.lsxy.framework.api.recharge.enums;

/**
 * 充值支付状态
 * Created by liups on 2016/7/1.
 */
public enum RechargeStatus {
    NOTPAID("未支付"),PAID("已支付");
    private String name;
    private RechargeStatus(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
