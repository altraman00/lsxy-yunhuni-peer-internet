package com.lsxy.app.backend.handler;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailContentNullException;
import com.lsxy.framework.mail.MailService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.portal.SendActivePasswordSuccessEvent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/10/20.
 */
@Component
public class SendActivePasswordSuccessEventHandler implements MQMessageHandler<SendActivePasswordSuccessEvent> {
    private static final Logger logger = LoggerFactory.getLogger(SendActivePasswordSuccessEventHandler.class);

    @Autowired
    AccountService accountService;
    @Autowired
    MailService mailService;
    @Override
    public void handleMessage(SendActivePasswordSuccessEvent message) throws JMSException {
        try {
            //获取用户信息，发送账号密码
            Account account = accountService.findById(message.getKey());
            if(account!=null){
                String email = account.getEmail();
                //判断邮件是否存在
                if(StringUtils.isNotEmpty(email)&&StringUtils.isNotEmpty(message.getPassword())){
                    //发送邮件
                    Map<String,String> params = new HashMap<>();
                    params.put("host", SystemConfig.getProperty("portal.system.root.url"));
                    params.put("resPrefixUrl", SystemConfig.getProperty("global.resPrefixUrl"));
                    params.put("date", DateUtils.getDate("yyyy年MM月dd日"));
                    params.put("globalOfficialWebsiteUrl", SystemConfig.getProperty("global.official.website.url"));
                    params.put("phone","400-002-0048");
                    params.put("password",message.getPassword());
                    //↓↓↓↓↓测试环境专用，往测试人员发邮件--start-->
                    String testEmail = SystemConfig.getProperty("global.mail.tester.email");
                    if(StringUtils.isNotBlank(testEmail)){
                        mailService.send("重置密码",testEmail,"04-portal-notify-activate-password.vm",params);
                    }
                    //↑↑↑↑↑测试环境专用，往测试人员发邮件--end-->
                    mailService.send("重置密码",email,"04-portal-notify-activate-password.vm",params);
                    //将参数和对应的邮箱存取redis里
                    long expireTime = Long.parseLong(SystemConfig.getProperty("account.email.expire","72"));
                    if(logger.isDebugEnabled()){
                        logger.debug("激活账号成功发送密码邮件：email:{},password:{}",email,message.getPassword());
                    }
                }else{
                    logger.error("密码或者邮件为空：email:{},password:{}",email,message.getPassword());
                }
            }else{
                logger.error("找不到对应的用户对象：key:{},account:{}",message.getKey(),account);
            }
        } catch (MailConfigNotEnabledException e) {
            logger.error("找不到有效的邮件配置",e);
        } catch (MailContentNullException e) {
            logger.error("邮件内容为空！",e);
        }
    }
}
