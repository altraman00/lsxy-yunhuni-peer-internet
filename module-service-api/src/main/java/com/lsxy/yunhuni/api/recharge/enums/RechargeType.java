package com.lsxy.yunhuni.api.recharge.enums;

/**
 * 充值类型，一定要是规定好的类型：支付宝，银联
 * Created by liups on 2016/7/1.
 */
public enum RechargeType {
    ZHIFUBAO("支付宝"),YINLIAN("银联");
    private String name;
    RechargeType(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
