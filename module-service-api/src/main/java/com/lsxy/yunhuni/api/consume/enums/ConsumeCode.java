package com.lsxy.yunhuni.api.consume.enums;

/**
 * 消费类型code，一部份与产品一致
 * Created by liups on 2016/9/20.
 */
public enum ConsumeCode {
    duo_call("语音回拔"),
    sys_conf("语音会议"),
    ivr_call("自定义IVR"),
    captcha_call("语音验证码"),
    notify_call("语音通知"),
    call_center("云呼叫中心"),
    recording_memory("录音文件存储费"),
    call_voice("通话录音"),
    rent_number("号码租用"),
    rent_number_month("号码月租费"),
    flat_balance("平账"),
    call_center_month("云呼叫中心-坐席月租费");

    private String name;
    ConsumeCode(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
