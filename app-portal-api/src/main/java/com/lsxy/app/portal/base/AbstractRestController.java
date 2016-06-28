package com.lsxy.app.portal.base;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.tenant.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Created by Tandy on 2016/6/14.
 * 抽象控制器类，提供获取当前登录用户的方法
 */
public abstract class AbstractRestController {
    @Autowired
    AccountService accountService;
    protected Account getCurrentAccount() throws MatchMutiEntitiesException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountService.findPersonByLoginName(user.getUsername());
    }

}
