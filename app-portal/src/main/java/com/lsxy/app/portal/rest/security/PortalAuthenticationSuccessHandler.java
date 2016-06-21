package com.lsxy.app.portal.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by liups on 2016/6/21.
 */
public class PortalAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public static final String SSO_TOKEN = "X-Auth-Token";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        logger.debug("user login success:"+authentication.getName());
        //将tocken保存到session
        Object details = authentication.getDetails();
        if(details instanceof String){
            HttpSession session = request.getSession(false);
            if(session != null) {
                session.setAttribute(SSO_TOKEN,details);
            }
        }
    }
}
