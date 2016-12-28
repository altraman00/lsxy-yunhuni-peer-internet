package com.lsxy.yunhuni.api.product.enums;

/**
 * 产品计费项标识码，一个产品计费项标识码严格对应一种产品计费项，消费类型和会话类型也使用 枚举.name()存入
 * Created by liups on 2016/8/30.
 */
public enum ProductCode {
    notify_call("notify_call","语音通知"),
    captcha_call("captcha_call,verify_call","语音验证码"),
    duo_call("duo_call","语音回拔"),
    ivr_call("ivr_call,ivr_incoming,ivr_dial","自定义IVR"),
    sys_conf("sys_conf","语音会议"),
    recording("recording","录音服务"),
    recording_memory("recording_memory","录音文件存储策略"),
    rent_number("rent_number","号码占用费"),
    rent_number_month("rent_number_month","号码月租费"),
    call_center("cc_incoming,conversation,agent_call,out_call","呼叫中心"),
    call_center_month("call_center_month","坐席月租费");

    private String apiCmd;  //产品和api的对应关系，一个产品可以对应多个apiCmd，多个apiCmd用“,”号隔开，如 sys_conf("sys_conf,sys_conf,conf_XXXX","语音会议"),
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
    public static String getApiCmdByRemark(String remark){
        ProductCode[] values = ProductCode.values();
        for(ProductCode value:values){
            if(value.getRemark().equals(remark)){
                return value.name();
            }
        }
        return null;
    }

}
