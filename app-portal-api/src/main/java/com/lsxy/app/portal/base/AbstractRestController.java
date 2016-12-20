package com.lsxy.app.portal.base;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.OssTempUriUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;

/**
 * Created by Tandy on 2016/6/14.
 * 抽象控制器类，提供获取当前登录用户的方法
 */
public abstract class AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRestController.class);
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
        logger.error("异常",ex);
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

    protected String getOssTempUri(String resource){
        String host = SystemConfig.getProperty("global.oss.aliyun.endpoint.internet","http://oss-cn-beijing.aliyuncs.com");
        String accessId = SystemConfig.getProperty("global.aliyun.key","nfgEUCKyOdVMVbqQ");
        String accessKey = SystemConfig.getProperty("global.aliyun.secret","HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW");
        String resource1 = SystemConfig.getProperty("global.oss.aliyun.bucket");
        try {
            URL url = new URL(host);
            host = url.getHost();
        }catch (Exception e){}
        resource = "/"+resource1+"/"+resource;
        String result = OssTempUriUtils.getOssTempUri( accessId, accessKey, host, "GET",resource ,60);
        return result;
    }
}
