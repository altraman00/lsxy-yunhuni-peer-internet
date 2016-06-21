package com.lsxy.app.portal.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tandy on 2016/6/20.
 * 预授权，通过token授权
 */
public class TokenPreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter{
    private static final String SSO_TOKEN = "X-YUNHUNI-API-TOKEN";
    public static final String SSO_CREDENTIALS = "N/A";

    public TokenPreAuthenticationFilter(AuthenticationManager authenticationManager) {
        setAuthenticationManager(authenticationManager);
    }


    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(SSO_TOKEN);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return SSO_CREDENTIALS;
    }
}
