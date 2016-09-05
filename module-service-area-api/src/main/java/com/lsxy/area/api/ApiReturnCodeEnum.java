package com.lsxy.area.api;

/**
 * Created by liuws on 2016/8/27.
 * 定义返回值常量（成功或各种异常）
 */
public enum ApiReturnCodeEnum {
    Success("000000","请求成功"),
    UnknownFail("111111","未知错误"),
    AppServiceInvalid("101010","app没开通该服务"),
    BalanceNotEnough("101011","余额不足"),
    InvokeCall("101012","调用失败"),
    IPNotInWhiteList("101013","IP不在白名单内"),
    NumberNotAllowToCall("101014","号码不允许呼叫"),
    ConfNotExists("101015","会议不存在");

    private String code;
    private String msg;
    private ApiReturnCodeEnum(String code, String message){
        this.code = code;
        this.msg = message;
    }
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
