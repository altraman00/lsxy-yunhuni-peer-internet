package com.lsxy.app.portal.base;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Created by Tandy on 2016/6/14.
 * 抽象控制器类，提供获取当前登录用户的方法
 */
public abstract class AbstractRestController {
    @Autowired
    AccountService accountService;

    /**
     * 获取当前用户账号信息
     * @return 当前用户账号信息
     */
    protected Account getCurrentAccount(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.findAccountByUserName(user.getUsername());
    }

    /**
     * 获取当前用户账号的用户名
     * @return 当前用户账号的用户名
     */
    protected String getCurrentAccountUserName(){
        //从登录通过的凭证中取出用户
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }
}
