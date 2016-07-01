package com.lsxy.app.portal.comm;

import com.lsxy.framework.config.SystemConfig;

/**
 * Created by liups on 2016/6/16.
 */
public class PortalConstants {

    //验证码保存在session里的key
    public static final String VC_KEY = "validateCode";

    //手机验证码保存在session里的key
    public static final String MC_KEY = "mobileCodeChecker";

    //请求前缀
    public static final String REST_PREFIX_URL = SystemConfig.getProperty("portal.rest.api.url","http://localhost:8080");

   //保存在session里的token的key
   public static final String SSO_TOKEN = SystemConfig.getProperty("global.rest.api.security.header","X-YUNHUNI-API-TOKEN");
}
