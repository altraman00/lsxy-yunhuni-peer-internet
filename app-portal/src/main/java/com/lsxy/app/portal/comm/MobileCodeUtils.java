package com.lsxy.app.portal.comm;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/6/25.
 */
public class MobileCodeUtils {
    public static MobileCodeChecker getMobileCodeChecker(HttpServletRequest request){
        Object obj = request.getSession().getAttribute(PortalConstants.MC_KEY);
        if(obj != null && obj instanceof MobileCodeChecker){
            return (MobileCodeChecker) obj;
        }else{
            return null;
        }
    }

    public static void setMobileCodeChecker(HttpServletRequest request,MobileCodeChecker checker){
        request.getSession().setAttribute(PortalConstants.MC_KEY,checker);
    }

    public static void removeMobileCodeChecker(HttpServletRequest request){
        request.getSession().removeAttribute(PortalConstants.MC_KEY);
    }
}
