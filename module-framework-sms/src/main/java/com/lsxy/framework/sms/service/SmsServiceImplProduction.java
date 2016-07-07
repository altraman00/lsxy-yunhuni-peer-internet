package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.sms.clients.SMSClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Tandy on 2016/6/29.
 * 生产环境使用
 * 生成环境，先短信网关发短信，然后异步执行入库处理
 */

@Profile({"production"})
@Component
public class SmsServiceImplProduction  extends AbstractSmsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImplProduction.class);


    @Autowired
    private SMSClientFactory smsClientFactory;

    @Override
    public boolean sendsms(String to, String template, Map<String, Object> params) {

        String content = buildSmsContent(template,params);
        if(logger.isDebugEnabled()){
            logger.debug("发送短信给：{}",to);
            logger.debug("内容:{}",content);
        }
        boolean result =  smsClientFactory.getSMSClient().sendsms(to,content);

        if(logger.isDebugEnabled()){
            logger.debug("发送结果:{}",result);
        }
        if(result){
            //如果短信发送成功就异步存到数据库
            String clientName = smsClientFactory.getSMSClient().getClientName();
            SMSSendLog log = new SMSSendLog(to,content,clientName);
            saveToDB(log);
        }
        return result;
    }



}
