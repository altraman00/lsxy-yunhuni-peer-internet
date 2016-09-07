package com.lsxy.app.portal.base;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.exceptions.TokenMissingException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.security.SecurityUser;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/6/28.
 */
public abstract class AbstractPortalController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPortalController.class);
    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView exp(HttpServletRequest request,HttpServletResponse response,Exception ex) {
        ex.printStackTrace();
        ModelAndView mav;
        //Ajax请求带有X-Requested-With:XMLHttpRequest
        String xRequestedWith = request.getHeader("X-Requested-With");
        if (StringUtils.isNotBlank(xRequestedWith) && "XMLHttpRequest".equals(xRequestedWith)) {
            String result = JSONUtil.objectToJson(RestResponse.failed("0000",ex.getMessage()));
            try {
                response.getWriter().write(result);
            }catch (Exception e){}
            return null;
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
        if(logger.isDebugEnabled()){
            logger.debug("get security token is : {}" , obj);
        }
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

    /**
     * 获取当前用户（SecurityUser）
     * @param request
     * @return
     */
    public SecurityUser getCurrentUser(HttpServletRequest request){
        return (SecurityUser) request.getSession().getAttribute("currentUser");
    }
}
