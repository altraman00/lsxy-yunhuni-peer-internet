package com.lsxy.msg.api.service;

import java.util.Date;

/**
 * Created by liups on 2017/3/7.
 */
public interface MsgSendService {
    //单发闪印
    String sendUssd(String mobile,String tempId,String tempArgw);
    //群发闪印
    String sendMassUssd(String taskName,String tempId,String tempIdArgs, String mobiles, Date sendTime);
    //单发短信
    String sendSms(String mobile,String tempId,String tempArgw);
    //群发短信
    String sendMassSms(String taskName,String tempId,String tempIdArgs, String mobiles, Date sendTime);
}
