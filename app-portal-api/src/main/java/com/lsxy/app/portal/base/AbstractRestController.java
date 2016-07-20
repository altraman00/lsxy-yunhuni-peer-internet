package com.lsxy.app.portal.base;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tandy on 2016/6/14.
 * 抽象控制器类，提供获取当前登录用户的方法
 */
public abstract class AbstractRestController {
    @Autowired
    AccountService accountService;

    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String exp(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        RestResponse failed = RestResponse.failed("0000", ex.getMessage());
        return JSONUtil2.objectToJson(failed);
    }

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
