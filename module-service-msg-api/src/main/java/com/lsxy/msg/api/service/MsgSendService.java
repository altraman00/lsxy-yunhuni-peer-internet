package com.lsxy.msg.api.service;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/7.
 */
public interface MsgSendService {
    //单发闪印
    String sendUssd(String ip,String appId,String accountId,String mobile,String tempId,String tempArgs) throws YunhuniApiException;
    //群发闪印
    String sendUssdMass(String ip, String appId, String accountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr) throws YunhuniApiException;
    //单发短信
    String sendSms(String ip,String appId,String accountId,String mobile,String tempId,String tempArgs) throws YunhuniApiException;
    //群发短信
    String sendSmsMass(String ip, String appId, String accountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr) throws YunhuniApiException;

    void batchConsumeMsg(Date dt, String type, BigDecimal cost, String remark, String appId, String tenantId, String subaccountId, List<String> detailIds);
}
