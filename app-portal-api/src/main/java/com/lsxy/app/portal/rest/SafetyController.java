package com.lsxy.app.portal.rest;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by zhangxb on 2016/6/28.
 * 实名认证
 */
@RequestMapping("/rest/account/safety")
@RestController
public class SafetyController {
    @Autowired
    private AccountService accountService;
    private static final String IS_ERROR = "-2";//表示密码错误
    private static final String IS_FALSE = "-1";//表示失败
    private static final String IS_TRUE = "1";//表示成功

    /**
     * 验证手机号码是否正确
     * @param userName 用户名
     * @param password 待验证密码
     * @return
     */
    @RequestMapping("/validationPsw")
    public RestResponse validationPsw(String userName,String password){
        Account account = accountService.findByUserName(userName);
        String result = IS_FALSE;//表示密码错误
        if(password!=null&&password.length()>0){
            password =   PasswordUtil.springSecurityPasswordEncode(password,userName);
            if(password.equalsIgnoreCase(account.getPassword())){
                result = IS_TRUE;
            }
        }
        return RestResponse.success(result);
    }

    /**
     * 修改绑定手机号码
     * @param userName 用户名
     * @param mobile 新绑定手机号码
     * @return
     */
    @RequestMapping("/updateMobile")
    public RestResponse updateMobile(String userName,String mobile){
        Account account = accountService.findByUserName(userName);
        account.setMobile(mobile);
        account = accountService.save(account);
        return RestResponse.success(account);
    }

    /**
     * 修改密码方法
     * @param userName 用户名
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    @RequestMapping("/editPssword")
    public RestResponse editPssword(String userName,String oldPassword,String newPassword){
        Account account = accountService.findByUserName(userName);
        String result = IS_ERROR;//表示密码错误
        if(oldPassword!=null&&oldPassword.length()>0){
            // 密码加密
            oldPassword =   PasswordUtil.springSecurityPasswordEncode(oldPassword,userName);
            if(oldPassword.equalsIgnoreCase(account.getPassword())){
                result = IS_TRUE;
            }
        }
        if(IS_TRUE.equals(result)){
            account.setPassword(newPassword);
            account = accountService.save(account);
            if(account==null){
                result=IS_FALSE;
            }
        }
        return RestResponse.success(result);
    }
}
