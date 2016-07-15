package com.lsxy.app.portal.comm;

import com.lsxy.framework.web.rest.RestRequest;

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

    /**
     * 验证通过的手机将其存入session
     * @param request
     * @param checker
     */
    public static void setMobileCodeChecker(HttpServletRequest request,MobileCodeChecker checker){
        request.getSession().setAttribute(PortalConstants.MC_KEY,checker);
    }

    /**
     * 认证的后续功能完成后，将缓存清掉
     * @param request
     */
    public static void removeMobileCodeChecker(HttpServletRequest request){
        Object obj = request.getSession().getAttribute(PortalConstants.MC_KEY);
        if(obj != null && obj instanceof MobileCodeChecker){
            removeMobileCode(((MobileCodeChecker) obj).getMobile());
        }
        request.getSession().removeAttribute(PortalConstants.MC_KEY);
    }

    /**
     * restApi调用 删除缓存的手机验证码
     * @param mobile
     */
    private static void removeMobileCode(String mobile){
        //此处调用检验短信验证码RestApi
        String checkUrl = PortalConstants.REST_PREFIX_URL + "/code/remove_mobile_code?mobile={1}";
        RestRequest.buildRequest().get(checkUrl, Boolean.class, mobile);
    }

}
