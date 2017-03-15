package com.lsxy.yunhuni.api.app.service;

/**
 * Created by liuws on 2016/11/25.
 */
public enum ServiceType {
    VoiceDirectly("isVoiceDirectly","notify_call"),//是否语音直拨 0否，1是
    VoiceCallback("isVoiceCallback","duo_call"),//是否语音回拨0否，1是
    SessionService("isSessionService","sys_conf"),//是否会议服务0否，1是
    Recording("isRecording","recording"),//是否录音服务0否，1是
    VoiceValidate("isVoiceValidate","captcha_call"),//是否语音验证码0否，1是
    IvrService("isIvrService","ivr_call"),//是否IVR定制服务0否，1是
    CallCenter("isCallCenter","call_center"),//是否呼叫中心
    NUMBERRESOURCE(null,"number_resource"),//号码资源是产品不是具体的服务 所以第一个参数为null
    SMS("isSms","sms"),//短信
    USSD("isUssd","ussd");//闪印

    private String code = null;
    private String product = null;

    /**
     * @param type 功能
     * @param product 产品
     */
    ServiceType(String type,String product)
    {
        this.code = type;
        this.product = product;
    }

    public String getCode() {
        return code;
    }

    public String getProduct() {
        return product;
    }
}
