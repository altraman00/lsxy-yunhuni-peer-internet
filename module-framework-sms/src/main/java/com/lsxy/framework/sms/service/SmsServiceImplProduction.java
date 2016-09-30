package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.sms.clients.SMSClientFactory;
import com.lsxy.framework.sms.exceptions.CheckCodeNotFoundException;
import com.lsxy.framework.sms.exceptions.CheckOutMaxTimesException;
import com.lsxy.framework.sms.exceptions.InvalidValidateCodeException;
import com.lsxy.framework.sms.exceptions.TooManyGenTimesException;
import com.lsxy.framework.sms.model.MobileCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

/**
 * Created by Tandy on 2016/6/29.
 * 生产环境使用
 * 生成环境，先短信网关发短信，然后异步执行入库处理
 * 测试环境为保证功能正确性,可使用网关进行发送
 */

@Profile({"production","test"})
@Component
public class SmsServiceImplProduction  extends AbstractSmsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImplProduction.class);


    @Autowired
    private AsyncSmsSaveTask asyncSmsSaveTask;

    @Autowired
    private SMSClientFactory smsClientFactory;

    @Autowired
    RedisCacheService redisCacheService;


    public RedisCacheService getRedisCacheService(){
        return this.redisCacheService;
    }

    @Override
    public boolean sendsms(String to, String template, Map<String, Object> params) {

        String content = buildSmsContent(template,params);
        if(logger.isDebugEnabled()){
            logger.debug("发送短信给：{}",to);
            logger.debug("内容:{}",content);
        }
        String result =  smsClientFactory.getSMSClient().sendsms(to,content);

        if(logger.isDebugEnabled()){
            logger.debug("发送结果:{}",result);
        }
        if(StringUtils.isNotEmpty(result)){
            //如果短信发送成功就异步存到数据库
            String clientName = smsClientFactory.getSMSClient().getClientName();
            SMSSendLog log = new SMSSendLog(to,content,clientName,result);
            asyncSmsSaveTask.saveToDB(log);
            return true;
        }else{
            logger.error("短信发送失败，发送对象["+to+"]发送内容["+content+"]");
            return false;
        }
    }

}
