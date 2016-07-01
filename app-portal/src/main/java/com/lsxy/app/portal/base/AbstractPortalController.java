package com.lsxy.app.portal.base;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.exceptions.TokenMissingException;

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
}
