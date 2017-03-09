package com.lsxy.msg.api.service;

/**
 * Created by liups on 2017/3/7.
 */
public interface MsgSendService {
    //单发闪印
    String sendUssd(String tenantId,String appId,String accountId,String mobile,String tempId,String tempArgs);
    //群发闪印
    String sendMassUssd(String tenantId,String appId,String accountId,String taskName,String tempId,String tempArgs, String mobiles, String sendTimeStr);
    //单发短信
    String sendSms(String tenantId,String appId,String accountId,String mobile,String tempId,String tempArgs);
    //群发短信
    String sendMassSms(String tenantId,String appId,String accountId,String taskName,String tempId,String tempArgs, String mobiles, String sendTimeStr);
}
