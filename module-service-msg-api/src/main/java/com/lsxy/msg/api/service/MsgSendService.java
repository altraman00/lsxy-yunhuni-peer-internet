package com.lsxy.msg.api.service;

/**
 * Created by liups on 2017/3/7.
 */
public interface MsgSendService {
    //单发闪印
    String sendUssd(String ip,String appId,String accountId,String mobile,String tempId,String tempArgs);
    //群发闪印
    String sendUssdMass(String ip, String appId, String accountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr);
    //单发短信
    String sendSms(String ip,String appId,String accountId,String mobile,String tempId,String tempArgs);
    //群发短信
    String sendSmsMass(String ip, String appId, String accountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr);
}
