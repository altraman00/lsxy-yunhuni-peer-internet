package com.lsxy.app.backend.handler;

import com.lsxy.framework.api.events.ResetPwdVerifySuccessEvent;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailContentNullException;
import com.lsxy.framework.mail.MailService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/7/27.
 */
@Component
public class ResetPwdVerifySuccessEventHandler implements MQMessageHandler<ResetPwdVerifySuccessEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ResetPwdVerifySuccessEventHandler.class);

    @Autowired
    private RedisCacheService cacheManager;

    @Autowired
    MailService mailService;

    @Override
    public void handleMessage(ResetPwdVerifySuccessEvent message) throws JMSException {
        try {
            String email = message.getEmail();
            //发送邮件（参数是一个UUID），并将其存到数据库（redis?）
            String key = "reset_pwd_" + UUIDGenerator.uuid();
            //TODO MQ事件，发送邮件
            Map<String,String> params = new HashMap<>();
            params.put("host", SystemConfig.getProperty("portal.system.root.url"));
            params.put("resPrefixUrl", SystemConfig.getProperty("global.resPrefixUrl"));
            params.put("key",key);
            params.put("date", DateUtils.getDate("yyyy年MM月dd日"));
            mailService.send("重置密码",email,"02-portal-notify-reset-password.vm",params);
            cacheManager.set(key,email,72 * 60 * 60);
            if(logger.isDebugEnabled()){
                logger.debug("邮件重置密码：code:{},email:{}",key,email);
            }
        } catch (MailConfigNotEnabledException e) {
            e.printStackTrace();
        } catch (MailContentNullException e) {
            e.printStackTrace();
        }
    }
}
