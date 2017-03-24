package com.lsxy.msg.api.service;

import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.msg.api.result.MsgSendMassResult;
import com.lsxy.msg.api.result.MsgSendOneResult;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/7.
 */
public interface MsgSendService {
    //单发闪印
    MsgSendOneResult sendUssd(String appId, String accountId, String mobile, String tempId, String tempArgs) throws YunhuniApiException;
    //群发闪印
    MsgSendMassResult sendUssdMass(String appId, String accountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr) throws YunhuniApiException;
    //单发短信
    MsgSendOneResult sendSms(String appId, String accountId, String mobile, String tempId, String tempArgs) throws YunhuniApiException;
    //群发短信
    MsgSendMassResult sendSmsMass(String appId, String accountId, String taskName, String tempId, String tempArgs, String mobiles, String sendTimeStr) throws YunhuniApiException;

    void batchConsumeMsg(Date dt, String type, BigDecimal cost, String remark, String appId, String tenantId, String subaccountId, List<String> detailIds);
}
