package com.lsxy.app.api.gateway.security.ip;

import com.lsxy.app.api.gateway.security.auth.AuthenticationRequestWrapper;
import com.lsxy.app.api.gateway.security.auth.RestAuthenticationEntryPoint;
import com.lsxy.app.api.gateway.security.auth.RestCredentials;
import com.lsxy.app.api.gateway.security.auth.RestToken;
import com.lsxy.framework.api.config.service.ApiGwBlankIPService;
import com.lsxy.framework.web.utils.IP;
import com.lsxy.framework.web.utils.WebUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.IpAddressMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import static com.lsxy.framework.web.utils.WebUtils.getRemoteAddress;

/**
 * Created by Tandy on 2016/7/4
 * 黑名单
 */

@Component
public class BlackIpListFilter extends GenericFilterBean{
    private static final Logger logger = LoggerFactory.getLogger(BlackIpListFilter.class);

    @Autowired
    private BlackIpEntryPoint authenticationEntryPoint;

    @Autowired
    private ApiGwBlankIPService apiGwBlankIPService;



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String ip = WebUtils.getRemoteAddress((HttpServletRequest) request);

        if(logger.isDebugEnabled()){
            logger.debug("进入IP黑名单校验过滤器");
        }
        boolean result = apiGwBlankIPService.isBlankIP(ip);
        if(logger.isDebugEnabled()){
            logger.debug("校验IP黑名单：{}  已被黑名单？ {}",ip,result);
        }
        if(!result){
            chain.doFilter(request,response);
        }else{
            IPAuthenticationException ex = new IPAuthenticationException("IP地址已被列入黑名单:"+ip);
            authenticationEntryPoint.commence((HttpServletRequest)request,(HttpServletResponse) response,ex);
        }
    }
}
