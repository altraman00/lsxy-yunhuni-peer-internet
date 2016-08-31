package com.lsxy.yunhuni.api.product.enums;

/**
 * Created by liups on 2016/8/30.
 */
public enum ProductCode {
    duo_call("duo_call1","双向回拔"),conf_call("conf_call","语音会议"),ivr_call("ivr_call","语音IVR"),captcha_call("captcha_call","语音验证码"),notify_call("notify_call","语音通知");
    private String apiCmd;
    private String remark;
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
