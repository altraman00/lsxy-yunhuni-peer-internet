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
 简单呼叫Api 03
 会议相关 0301
 短信相关 0302
 语音验证码相关 0303
 语音通知相关 0304
 双向回拨 0305
 呼叫中心 04
 比如： CallNotExists("020000","呼叫不存在"), 呼叫不存在代码可以设置为： 020116
 */
public enum ApiReturnCodeEnum {
    Success("000000","请求成功"),
    IllegalArgument("000001","参数错误"),
    UnknownFail("000002","未知错误"),
    InvokeCall("000003","内部错误"),
    SystemBusy("000004","系统繁忙"),
    AppNotFound("010000","应用不存在"),
    AppOffLine("010001","应用没上线"),
    AppServiceInvalid("010002","app没开通该服务"),
    BalanceNotEnough("010003","余额不足"),
    IPNotInWhiteList("010004","IP不在白名单内"),
    PlayFileNotExists("010005","放音文件不存在或未审核"),
    QuotaNotEnough("010006","配额不足"),
    CallNotExists("020000","呼叫不存在"),
    NumberNotAllowToCall("020001","号码不允许呼叫"),
    NotAvailableLine("020002","没有可用线路"),
    UserNumberHasNotAvailableLine("020002","用户号码没有可用线路"),
    ConfNotExists("030100","会议不存在"),
    OutOfConfMaxParts("030101","超过最大与会数"),
    DuoCallbackNumIsSample("030500","双向回拔的号码不能是同一号码"),
    ExtensionUserExist("040101","分机号已存在"),
    ExtensionBindingToAgent("040102","分机已绑定座席"),
    ExtensionNotExist("040103","分机不存在"),
    ExtensionUnEnable("040104","分机不可用"),
    AgentHasAlreadyLoggedIn("040201","座席已经登录"),
    AgentNotExist("040202","座席不存在"),
    AgentIsBusy("040203","座席正忙"),
    AgentExpired("040204","坐席不可用(过期)"),
    ConditionNotExist("040401","条件不存在"),
    ConditionExpression("040402","条件表达式错误"),
    ConversationNotExist("040501","交谈不存在"),
    OutOfConversationMaxParts("040502","超过最大交谈成员数"),
    AgentNotConversationMember("040503","坐席不是交谈的成员"),
    QueueTaskNotExist("040601","排队任务不存在"),
    MsgIllegalMobile("050101","手机号码不合法"),
    MsgTemplateError("050102","模板不存在或模板审核不通过"),
    MsgTaskNameIsEmpty("050103","任务名不能为空"),
    MsgTemplateArgsError("050104","模板参数不正确"),
    MsgMobileNumTooLarge("050105","手机号码集合数量大于最大限制"),
    MsgSendMsgFail("050105","发送消息失败"),
    MsgOperatorNotAvailable("050106","本平台对号码所属运营商暂时不支持该发送类型"),
    MsgSendTimeFormatError("050107","发送时间格式错误，格式必须是：yyyyMMddHHmmss"),
    MsgContentTooLarge("050108","消息长度过长")
    ;

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
