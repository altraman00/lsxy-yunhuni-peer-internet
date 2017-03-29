package com.lsxy.area.server.mq.handler.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.msg.TemplateCompleteEvent;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Map;

/**
 * Created by liups on 2017/3/23.
 */
@Component
public class TemplateCompleteEventHandler implements MQMessageHandler<TemplateCompleteEvent> {
    private static final Logger logger = LoggerFactory.getLogger(TemplateCompleteEventHandler.class);
    @Reference(timeout=3000,check = false,lazy = true)
    MsgTemplateService msgTemplateService;
    @Autowired
    ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Autowired
    AppService appService;
    @Autowired
    NotifyCallbackUtil notifyCallbackUtil;

    @Override
    public void handleMessage(TemplateCompleteEvent message) throws JMSException {

        if(logger.isDebugEnabled()){
            logger.debug("模板完成回调事件{}",message.toJson());
        }
        String tempId = message.getTempId();
        MsgTemplate template = msgTemplateService.findByTempId(tempId);
        if(MsgTemplate.STATUS_PASS == template.getStatus() ||  MsgTemplate.STATUS_FAIL == template.getStatus()){

            String subaccountId = template.getSubaccountId();
            String callbackUrl = null;
            if(StringUtils.isNotBlank(subaccountId)){
                ApiCertificateSubAccount subAccount = apiCertificateSubAccountService.findById(subaccountId);
                if(subAccount != null && StringUtils.isNotBlank(subAccount.getCallbackUrl())){
                    callbackUrl = subAccount.getCallbackUrl();
                }
            }

            if(StringUtils.isBlank(callbackUrl)){
                App app = appService.findById(template.getAppId());
                callbackUrl = app.getUrl();
            }
            if(StringUtils.isNotBlank(callbackUrl)){
                if(logger.isDebugEnabled()){
                    logger.debug("回调地址{}",callbackUrl);
                }
                Map<String,Object> notify_data = new MapBuilder<String,Object>()
                        .putIfNotEmpty("event","msg.template_complete")
                        .putIfNotEmpty("tempId",tempId)
                        .putIfNotEmpty("state",MsgTemplate.STATUS_PASS == template.getStatus()?1:0)
                        .putIfNotEmpty("reason",template.getReason())
                        .build();
                // 发送通知
                notifyCallbackUtil.postNotify(callbackUrl,notify_data,null,3);
            }
        }

        if(logger.isDebugEnabled()){
            logger.debug("模板完成回调事件完成{}",message.toJson());
        }
    }
}
