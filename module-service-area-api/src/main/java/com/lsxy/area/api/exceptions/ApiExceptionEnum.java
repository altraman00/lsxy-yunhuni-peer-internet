package com.lsxy.area.api.exceptions;

/**
 * Created by liuws on 2016/8/27.
 * 定义异常常量
 */
public enum ApiExceptionEnum {
    AppServiceInvalid("101010","app没开通该服务"),
    BalanceNotEnough("101011","余额不足"),
    InvokeCall("101012","调用失败"),
    IPNotInWhiteList("101013","IP不在白名单内"),
    NumberNotAllowToCall("101014","号码不允许呼叫"),
    ConfNotExists("101015","会议不存在");

    private String errorCode;
    private String errorMsg;
    private ApiExceptionEnum(String code, String message){
        this.errorCode = code;
        this.errorMsg = message;
    }
    public String getCode() {
        return errorCode;
    }

    public String getMsg() {
        return errorMsg;
    }
}
