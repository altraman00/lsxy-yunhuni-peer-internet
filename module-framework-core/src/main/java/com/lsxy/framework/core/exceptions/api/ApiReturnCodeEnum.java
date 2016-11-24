package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liuws on 2016/8/27.
 * 定义返回值常量（成功或各种异常）
 */

/**
 错误代码分为6位 前2位代表分类 中间两位代表二级分类 最后2为为异常序号
 公共 00
 应用相关 01
 呼叫相关 02
 会议相关 03
 短信相关 04
 语音验证码相关 05
 语音通知相关 06
 双向回拨 07
 呼叫中心 08
 比如： CallNotExists("020000","呼叫不存在"), 呼叫不存在代码可以设置为： 020116
 */
public enum ApiReturnCodeEnum {
    Success("000000","请求成功"),
    IllegalArgument("000001","参数错误"),
    UnknownFail("000002","未知错误"),
    InvokeCall("000003","内部错误"),
    AppNotFound("010000","应用不存在"),
    AppOffLine("010001","应用没上线"),
    AppServiceInvalid("010002","app没开通该服务"),
    BalanceNotEnough("010003","余额不足"),
    IPNotInWhiteList("010004","IP不在白名单内"),
    PlayFileNotExists("010005","放音文件不存在或未审核"),
    CallNotExists("020000","呼叫不存在"),
    NumberNotAllowToCall("020001","号码不允许呼叫"),
    ConfNotExists("030000","会议不存在"),
    OutOfConfMaxParts("030001","超过最大与会数"),
    DuoCallbackNumIsSample("070000","双向回拔的号码不能是同一号码"),
    ExtensionUserExist("080101","分机账号已存在"),
    ExtensionBindingToAgent("080102","分机已绑定座席"),
    ExtensionNotExist("080103","分机不存在"),
    AgentHasAlreadyLoggedIn("080201","座席已经登录"),
    AgentNotExist("080202","座席不存在"),
    AgentIsBusy("080203","座席正忙");


    private String code;
    private String msg;
    ApiReturnCodeEnum(String code, String message){
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
