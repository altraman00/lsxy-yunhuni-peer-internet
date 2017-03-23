package com.lsxy.area.server.mq.handler.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.msg.MsgRequestCompletedEvent;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import java.util.Map;

/**
 * Created by liups on 2017/3/23.
 */
public class MsgRequestCompletedEventHandler implements MQMessageHandler<MsgRequestCompletedEvent> {

    @Reference(timeout=3000,check = false,lazy = true)
    MsgUserRequestService msgUserRequestService;
    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendDetailService msgSendDetailService;
    @Autowired
    NotifyCallbackUtil notifyCallbackUtil;
    @Autowired
    ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Autowired
    AppService appService;
    @Override
    public void handleMessage(MsgRequestCompletedEvent message) throws JMSException {
        String msgKey = message.getMsgKey();
        MsgUserRequest request = msgUserRequestService.findByMsgKey(msgKey);
        String subaccountId = request.getSubaccountId();
        String callbackUrl = null;
        if(StringUtils.isNotBlank(subaccountId)){
            ApiCertificateSubAccount subAccount = apiCertificateSubAccountService.findById(subaccountId);
            if(subAccount != null && StringUtils.isNotBlank(subAccount.getCallbackUrl())){
                callbackUrl = subAccount.getCallbackUrl();
            }
        }

        if(StringUtils.isBlank(callbackUrl)){
            App app = appService.findById(request.getAppId());
            callbackUrl = app.getUrl();
        }
        if(StringUtils.isBlank(callbackUrl)){
            String failMobiles = msgSendDetailService.findFailMobilesByMsgKey(msgKey);
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","msg.sending_complete")
                    .putIfNotEmpty("msgKey",msgKey)
                    .putIfNotEmpty("state",request.getState())
                    .putIfNotEmpty("failMobiles",failMobiles)
                    .putIfNotEmpty("url",callbackUrl)
                    .build();
            // 发送通知
            notifyCallbackUtil.postNotify(callbackUrl,notify_data,null,3);
        }

    }

}
