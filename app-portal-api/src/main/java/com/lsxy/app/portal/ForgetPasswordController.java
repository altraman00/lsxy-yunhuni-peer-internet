package com.lsxy.app.portal;

import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailContentNullException;
import com.lsxy.framework.mail.MailService;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 忘记密码
 * Created by liups on 2016/7/7.
 */
@RestController
@RequestMapping("/forget")
public class ForgetPasswordController {
    private static final Logger logger = LoggerFactory.getLogger(ForgetPasswordController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private RedisCacheService cacheManager;

    @Autowired
    MailService mailService;

    /**
     * 忘记密码-检查邮箱是否存在
     * @param email
     * @return
     */
    @RequestMapping("/check_email")
    public RestResponse checkEmail(String email){
        boolean isExist = accountService.checkEmail(email);
        return  RestResponse.success(isExist);
    }

    /**
     * 忘记密码-检查手机是否存在
     * @param mobile
     * @return
     */
    @RequestMapping("/check_mobile")
    public RestResponse checkMobile(String mobile){
        boolean isExist = accountService.checkMobile(mobile);
        return  RestResponse.success(isExist);
    }

    /**
     * 忘记密码-发送邮件,存到redis里key为uuid，value为邮箱
     * @param email
     * @return
     */
    @RequestMapping("/send_email")
    public RestResponse sendEmail(String email) throws MailConfigNotEnabledException, MailContentNullException {
        //发送邮件（参数是一个UUID），并将其存到数据库（redis?）
        String uuid = UUIDGenerator.uuid();

        //TODO MQ事件，发送邮件
        Map<String,String> params = new HashMap<>();
        params.put("host", SystemConfig.getProperty("portal.system.root.url"));
        params.put("resPrefixUrl", SystemConfig.getProperty("global.resPrefixUrl"));
        params.put("key",uuid);
        params.put("date", DateUtils.getDate("yyyy年MM月dd日"));
        mailService.send("重置密码",email,"02-portal-notify-reset-password.vm",params);
        cacheManager.set(uuid,email,72 * 60 * 60);
        if(logger.isDebugEnabled()){
            logger.debug("邮件重置密码：code:{},email:{}",uuid,email);
        }
        return RestResponse.success(null);
    }

    /**
     * 根据邮箱来更改密码
     * @param email 邮箱
     * @param password 新的密码
     * @return
     */
    @RequestMapping("/reset_pwd_email")
    public RestResponse resetPwdByEmail(String email,String password){
        accountService.resetPwdByEmail(email,password);
        return RestResponse.success(null);
    }

    /**
     * 根据手机来更改密码
     * @param mobile 手机
     * @param password 新的密码
     * @return
     */
    @RequestMapping("/reset_pwd_mobile")
    public RestResponse resetPwdByMobile(String mobile,String password){
        accountService.resetPwdByMobile(mobile,password);
        return RestResponse.success(null);
    }

}
