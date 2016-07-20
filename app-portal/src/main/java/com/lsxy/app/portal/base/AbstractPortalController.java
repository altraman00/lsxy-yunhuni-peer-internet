package com.lsxy.app.portal.base;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.exceptions.TokenMissingException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.lsxy.framework.cache.FrameworkCacheConfig.logger;

/**
 * Created by liups on 2016/6/28.
 */
public abstract class AbstractPortalController {

    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exp(HttpServletRequest request,Exception ex) {
        ex.printStackTrace();
        ModelAndView mav;
        //Ajax请求带有X-Requested-With:XMLHttpRequest
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(xRequestedWith) && "XMLHttpRequest".equals(xRequestedWith)) {
            // ajax请求
            Map<String,Object> model = new HashMap<>();
            model.put("flag",false);
            model.put("msg",ex.getMessage());
            mav = new ModelAndView("ajax_error",model);
        }else{
            mav = new ModelAndView("error_page");
        }
        return mav;
    }

    /**
     * 获取当前登录用户的授权token
     * @param request
     * @return
     */
    public String getSecurityToken(HttpServletRequest request){
        String token = null;
        Object obj = request.getSession().getAttribute(PortalConstants.SSO_TOKEN);
        if(obj != null && obj instanceof String){
            token = (String) obj;
        }
        if(token == null){
            throw new TokenMissingException("token丢失");
        }
        return token;
    }

    /**
     * 获取用户的当前对象
     * @param request
     * @return
     */
    public Account getCurrentAccount(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/account/get/current";
        RestResponse<Account> restResponse = RestRequest.buildSecurityRequest(token).get(uri, Account.class);
        Account account = restResponse.getData();
        return account;
    }
}
