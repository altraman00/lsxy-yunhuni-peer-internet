package com.lsxy.app.api.gateway.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Tandy on 2016/6/30.
 * 签名认证过滤器
 */
public class SignatureAuthFilter extends GenericFilterBean{
    private static final Logger logger = LoggerFactory.getLogger(SignatureAuthFilter.class);
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(logger.isDebugEnabled()) {
            logger.debug("进入签名认证过滤器：" + request.getRequestURI());
        }

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
