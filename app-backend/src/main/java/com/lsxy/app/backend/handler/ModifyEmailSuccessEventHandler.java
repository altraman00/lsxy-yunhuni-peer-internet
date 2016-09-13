package com.lsxy.app.backend.handler;

import com.lsxy.framework.mq.events.portal.ModifyEmailSuccessEvent;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailContentNullException;
import com.lsxy.framework.mail.MailService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

/**
 * 修改邮件事件处理
 * Created by zhangxb on 2016/7/28.
 */
@Component
public class ModifyEmailSuccessEventHandler implements MQMessageHandler<ModifyEmailSuccessEvent>{
    private static final Logger logger = LoggerFactory.getLogger(ModifyEmailSuccessEventHandler.class);
    @Autowired
    AccountService accountService;
    @Autowired
    MailService mailService;
    @Autowired
    private RedisCacheService cacheManager;
    @Override
    public void handleMessage(ModifyEmailSuccessEvent message) throws JMSException {
        try {
            String accountId = message.getAccountId();
            Account account = accountService.findById(accountId);
            // 发送激活邮件
            String uuid = UUIDGenerator.uuid();
            Map<String,String> params = new HashMap<>();
            params.put("host", SystemConfig.getProperty("portal.system.root.url"));
            params.put("resPrefixUrl", SystemConfig.getProperty("global.resPrefixUrl"));
            params.put("id",account.getId());
            params.put("code",uuid);
            params.put("date", DateUtils.getDate("yyyy年MM月dd日 HH:mm"));
            mailService.send("重置邮箱",message.getEmail(),"03-portal-notify-modify-email.vm",params);
            //↓↓↓↓↓测试环境专用，往测试人员发邮件--start-->
            String testEmail = SystemConfig.getProperty("global.mail.tester.email");
            if(StringUtils.isNotBlank(testEmail)){
                mailService.send("重置邮箱",testEmail,"03-portal-notify-modify-email.vm",params);
            }
            //↑↑↑↑↑测试环境专用，往测试人员发邮件--end-->

            if(logger.isDebugEnabled()){
                logger.debug("重置邮件-发邮件，key：{},accountId:{}，userName:{}",uuid,account.getId(),account.getUserName());
            }
            //将邮件的校验信息存到redis里，前缀为account_modify_email_
            Map map = new HashMap();
            map.put("email",message.getEmail());
            map.put("code",uuid);
            String re = JSONUtil.mapToJson(map);
            cacheManager.set("account_modify_email_" + account.getId() ,re, 12*60*60);
        } catch (MailConfigNotEnabledException e) {
            e.printStackTrace();
        } catch (MailContentNullException e) {
            e.printStackTrace();
        }
    }
}

