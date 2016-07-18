package com.lsxy.app.portal.base;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.exceptions.TokenMissingException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/6/28.
 */
public abstract class AbstractPortalController {
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
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/account/get";
        RestResponse<Account> restResponse = RestRequest.buildSecurityRequest(token).get(uri, Account.class);
        Account account = restResponse.getData();
        return account;
    }
}
