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
    rent_number("号码租用"),
    flat_balance("平账");

    private String name;
    ConsumeCode(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
