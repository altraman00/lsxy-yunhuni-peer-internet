package com.lsxy.app.portal;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangxb on 2016/7/29.
 */
@RestController
@RequestMapping("/modify")
public class ModifyEmailController  {
    @Autowired
    private AccountService accountService;
    @Autowired
    private MQService mqService;
    /**
     * 修改邮箱
     * @param id 用户id
     * @param email 邮件号码
     * @return
     */
    @RequestMapping("/email")
    public RestResponse modifyEmail(String id, String email)   {
        Account account = accountService.findById(id);
        if(email.equals(account.getEmail())){
            return  RestResponse.failed("1003","该邮件已被使用");
        }
        boolean flag = accountService.checkEmail(email);
        if(flag){
            return RestResponse.failed("1004","该邮件已被使用");
        }
        account.setEmail(email);
        accountService.save(account);
        return RestResponse.success();
    }
}
