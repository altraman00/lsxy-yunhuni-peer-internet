package com.lsxy.yunhuni.api.consume.enums;

/**
 * 消费类型code，一部份与产品一致
 * Created by liups on 2016/9/20.
 */
public enum ConsumeCode {
    notify_call("语音通知"),
    captcha_call("语音验证码"),
    duo_call("语音回拔"),
    ivr_call("自定义IVR"),
    sys_conf("语音会议"),
    recording("通话录音"),
    recording_memory("录音文件存储费"),
    rent_number("号码租用"),
    rent_number_month("号码月租费"),
    call_center("呼叫中心-IP线路"),
    call_center_sip("呼叫中心-电话线路"),
    call_center_month("呼叫中心-坐席月租费"),
    flat_balance("平账"),
    msg_sms("短信"),
    msg_ussd("闪印");

    private String name;
    ConsumeCode(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
