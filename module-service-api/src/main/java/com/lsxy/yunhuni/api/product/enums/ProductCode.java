package com.lsxy.yunhuni.api.product.enums;

/**
 * 产品标识码，一个产品标识码严格对应一种产品，消费类型和会话类型也使用 枚举.name()存入
 * Created by liups on 2016/8/30.
 */
public enum ProductCode {
    duo_call("duo_call1","双向回拔"),
    conf_call("conf_call","语音会议"),
    ivr_call("ivr_call","语音IVR"),
    captcha_call("captcha_call","语音验证码"),
    notify_call("notify_call","语音通知");

    private String apiCmd;  //产品和api的对应关系，一个产品可以对应多个apiCmd，多个apiCmd用“,”号隔开，如 conf_call("conf_call,sys_conf,conf_XXXX","语音会议"),
    private String remark;  //产品备注
    ProductCode(String apiCmd,String remark){
        this.apiCmd = apiCmd;
        this.remark = remark;
    }
    public String getApiCmd(){
        return this.apiCmd;
    }
    public String getRemark(){
        return this.remark;
    }

    public static ProductCode changeApiCmdToProductCode(String apiCmd){
        ProductCode[] values = ProductCode.values();
        for(ProductCode value:values){
            if(value.getApiCmd().contains(apiCmd)){
                return value;
            }
        }
        return null;
    }

}
