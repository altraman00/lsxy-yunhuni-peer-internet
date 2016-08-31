package com.lsxy.yunhuni.api.product.enums;

/**
 * Created by liups on 2016/8/30.
 */
public enum ProductCode {
    duo_call("duo_call1"),conf_call("conf_call"),ivr_call("ivr_call"),captcha_call("captcha_call"),notify_call("notify_call");
    private String apiCmd;
    private ProductCode(String apiCmd){
        this.apiCmd = apiCmd;
    }
    public String getApiCmd(){
        return this.apiCmd;
    }

    public static String changeApiCmdToCode(String apiCmd){
        ProductCode[] values = ProductCode.values();
        for(ProductCode value:values){
            if(value.getApiCmd().contains(apiCmd)){
                String name = value.name();
                return name;
            }
        }
        return null;
    }
}
