package com.lsxy.app.portal.rest.base;

import com.lsxy.app.portal.rest.comm.PortalConstants;
import com.lsxy.app.portal.rest.exceptions.TokenMissingException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/6/28.
 */
public abstract class AbstractPortalController {
    public String getToken(HttpServletRequest request){
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
