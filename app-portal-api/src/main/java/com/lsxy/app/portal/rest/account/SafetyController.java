package com.lsxy.app.portal.rest.account;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.mq.events.portal.ModifyEmailSuccessEvent;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by zhangxb on 2016/6/28.
 * 实名认证
 */
@RequestMapping("/rest/account/safety")
@RestController
public class SafetyController extends AbstractRestController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MQService mqService;

    /**
     * 验证密码是否正确
     * @param password 待验证密码
     * @return
     */
    @RequestMapping("/validation_password")
    public RestResponse validationPassword(String password) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        Account account = accountService.findAccountByUserName(userName);
        if(password!=null&&password.length()>0){
            password =   PasswordUtil.springSecurityPasswordEncode(password,userName);
            if(password.equalsIgnoreCase(account.getPassword())){
                return RestResponse.success("1");
            }
        }
        return RestResponse.failed("1001","密码错误");
    }
    /**
     * 修改邮件-发送邮件
     * @param email 邮件
     * @return
     */
    @RequestMapping("/modify_email_bind")
    public RestResponse modifyEmailBind(String email)   {
        Account account = getCurrentAccount();
        if(email.equals(account.getEmail())){
            return  RestResponse.failed("1003","该邮件地址已被使用");
        }
        boolean flag = accountService.checkEmail(email);
        if(flag){
            return RestResponse.failed("1004","该邮件地址已被使用");
        }
        mqService.publish(new ModifyEmailSuccessEvent(account.getId(),email));
        return RestResponse.success();
    }



    /**
     * 修改绑定手机号码
     * @param mobile 新绑定手机号码
     * @return
     */
    @RequestMapping("/save_mobile")
    public RestResponse saveMobile( String mobile) {
        Account account = getCurrentAccount();
        if(mobile.equals(account.getMobile())){
            return  RestResponse.failed("1002","该手机号码已被使用");
        }
        boolean flag = accountService.checkMobile(mobile);
        if(flag){
            return RestResponse.failed("1001","该手机号码已被使用");
        }
        account.setMobile(mobile);
        account = accountService.save(account);
        return RestResponse.success(account);
    }

    /**
     * 修改密码方法
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @RequestMapping("/modify_pwd")
    public RestResponse modifyPwd(String oldPassword,String newPassword) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        Account account = accountService.findAccountByUserName(userName);
        if(StringUtils.isNotEmpty(oldPassword)){
            // 密码加密
            oldPassword =  PasswordUtil.springSecurityPasswordEncode(oldPassword,userName);
            newPassword = PasswordUtil.springSecurityPasswordEncode(newPassword,userName);
            if(oldPassword.equals(newPassword)){
                return RestResponse.failed("1003","新密码不能与原密码相同");
            }
            if(oldPassword.equals(account.getPassword())){
                account.setPassword(newPassword);
                account = accountService.save(account);
                if(account!=null){
                   return RestResponse.success("1");
                }else{
                    return RestResponse.failed("1002","修改数据库失败");
                }
            }else{
                return RestResponse.failed("1001","密码错误");
            }
        }else{
            return RestResponse.failed("1001","密码错误");
        }
    }
}
