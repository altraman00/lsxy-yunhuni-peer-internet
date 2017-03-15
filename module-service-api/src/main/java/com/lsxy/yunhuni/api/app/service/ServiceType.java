package com.lsxy.yunhuni.api.app.service;

/**
 * Created by liuws on 2016/11/25.
 */
public enum ServiceType {
    VoiceDirectly("isVoiceDirectly"),//是否语音直拨 0否，1是
    VoiceCallback("isVoiceCallback"),//是否语音回拨0否，1是
    SessionService("isSessionService"),//是否会议服务0否，1是
    Recording("isRecording"),//是否录音服务0否，1是
    VoiceValidate("isVoiceValidate"),//是否语音验证码0否，1是
    IvrService("isIvrService"),//是否IVR定制服务0否，1是
    CallCenter("isCallCenter"),//是否呼叫中心
    MSM("isMsm"),//短信
    USSD("isUssd");//闪印

    private String code = null;
    ServiceType(String type){
        code = type;
    }

    public String getCode() {
        return code;
    }
}
