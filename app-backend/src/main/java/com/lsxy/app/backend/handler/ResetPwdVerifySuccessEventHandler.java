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
 * 重置密码邮箱校验通过事件处理
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
            //存到redis里key为reset_pwd_{uuid}，value为邮箱
            String email = message.getEmail();
            //发送邮件（参数是一个reset_pwd_{uuid}），并将其存到数据库（redis?）
            String key = "reset_pwd_" + UUIDGenerator.uuid();
            //发送邮件
            Map<String,String> params = new HashMap<>();
            params.put("host", SystemConfig.getProperty("portal.system.root.url"));
            params.put("resPrefixUrl", SystemConfig.getProperty("global.resPrefixUrl"));
            params.put("key",key);
            params.put("date", DateUtils.getDate("yyyy年MM月dd日"));
            mailService.send("重置密码",email,"02-portal-notify-reset-password.vm",params);
            //将参数和对应的邮箱存取redis里
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
