package com.lsxy.yunhuni.api.recharge.enums;

/**
 * Created by liups on 2016/10/17.
 */
public enum  RechargeSource {
    USER("用户充值"),
    MANUAL_ACTIVITY("手工_活动赠送"),
    MANUAL_BUSINESS("手工_线下商务"),
    MANUAL_OTHER("手工_其他"),
    MANUAL_TEST("手工_测试");

    private String name;
    RechargeSource(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
