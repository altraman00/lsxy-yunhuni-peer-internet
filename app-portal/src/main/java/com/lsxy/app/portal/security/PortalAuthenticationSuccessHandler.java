package com.lsxy.app.portal.security;

import com.lsxy.app.portal.console.account.AccountController;
import com.lsxy.app.portal.comm.PortalConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.onAuthenticationSuccess(request, response, authentication);
        if(logger.isDebugEnabled()){
            logger.debug("user login success:{}",authentication.getName());
        }
        //将tocken保存到session
        Object details = authentication.getDetails();
        if(details instanceof String){
            HttpSession session = request.getSession(false);
            if(session != null) {
                session.setAttribute(PortalConstants.SSO_TOKEN,details);
            }
        }
    }
}
