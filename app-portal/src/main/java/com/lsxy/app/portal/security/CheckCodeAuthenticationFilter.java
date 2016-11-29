package com.lsxy.app.portal.security;

import com.lsxy.app.portal.comm.PortalConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CheckCodeAuthenticationFilter.class);
    //验证码超时
    public static final String VC_OVERTIME = "验证码超时";

    //验证码错误
    public static final String VC_ERROR = "验证码错误";
    //暗码,配置成为生产环境为空的状态
    private String hideCode;

    private String servletPath;
    public CheckCodeAuthenticationFilter(String servletPath,String failureUrl,String hideCode) {
        super(servletPath);
        this.servletPath= servletPath;
        this.hideCode = hideCode;
        setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler(failureUrl));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res=(HttpServletResponse)response;
        String expect = (String) req.getSession().getAttribute(PortalConstants.VC_KEY);
        if ("POST".equalsIgnoreCase(req.getMethod())&&servletPath.equals(req.getServletPath())){
            if(logger.isDebugEnabled()){
                logger.debug("校验图形验证码：当前验证码[{}],输入验证码[{}],暗码[{}]",req.getParameter(PortalConstants.VC_KEY),expect,hideCode);
            }
            //是否通过暗码验证
            boolean flag = false;
            //暗码校验，非生产环境可用start-↓↓↓↓↓↓↓↓--->
            if(StringUtils.isNotBlank(hideCode)){
                if(req.getParameter(PortalConstants.VC_KEY).equals(hideCode.substring(0,4))){
                    //正确，清空图形验证码
                    req.getSession().removeAttribute(PortalConstants.VC_KEY);
                    flag = true;
                }
            }
            //暗码校验，非生产环境可用end-↑↑↑↑↑↑↑↑↑--->
            if(!flag){
                if (expect == null) {
                    unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException(VC_OVERTIME));
                    return;
                } else if (!expect.equalsIgnoreCase(req.getParameter(PortalConstants.VC_KEY))) {
                    unsuccessfulAuthentication(req, res, new InsufficientAuthenticationException(VC_ERROR));
                    //图形验证码一次验证不过就清空
                    req.getSession().removeAttribute(PortalConstants.VC_KEY);
                    return;
                } else {
                    //清空图形验证码
                    req.getSession().removeAttribute(PortalConstants.VC_KEY);
                }
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
