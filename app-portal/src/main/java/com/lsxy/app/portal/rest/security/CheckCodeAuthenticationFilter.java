package com.lsxy.app.portal.rest.security;

import com.lsxy.app.portal.rest.comm.PortalConstants;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by liups on 2016/6/17.
 */
class CheckCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    //验证码超时
    public static final String VC_OVERTIME = "验证码超时";

    //验证码错误
    public static final String VC_ERROR = "验证码错误";

    private String servletPath;
    public CheckCodeAuthenticationFilter(String servletPath,String failureUrl) {
        super(servletPath);
        this.servletPath=servletPath;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res=(HttpServletResponse)response;

        if ("POST".equalsIgnoreCase(req.getMethod())&&servletPath.equals(req.getServletPath())){
            String expect = (String) req.getSession().getAttribute(PortalConstants.VC_KEY);
            if(expect == null){
                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException(VC_OVERTIME));
                return;
            }else if(!expect.equalsIgnoreCase(req.getParameter(PortalConstants.VC_KEY))){
                unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException(VC_ERROR));
                //图形验证码一次验证不过就清空
                req.getSession().removeAttribute(PortalConstants.VC_KEY);
                return;
            }
        }
        chain.doFilter(request,response);
    }

    //此处不用重写，因为重写了doFilter，这个方法不会被调用
    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        return null;
    }
}
