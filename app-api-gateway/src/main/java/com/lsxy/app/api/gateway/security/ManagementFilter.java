package com.lsxy.app.api.gateway.security;

import com.lsxy.app.api.gateway.security.auth.RestAuthenticationEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Tandy on 2016/7/4
 * 黑名单
 * 对象不能定义为bean组件，否则会被自动识别为servlet filter ，
 * 会导致spring security filter触发一次 servlet filter触发一次
 */

//@Component  不能打开该注释，否则会出问题
public class ManagementFilter extends OncePerRequestFilter{
    private static final Logger logger = LoggerFactory.getLogger(ManagementFilter.class);

    private RequestMatcher requestMatcher;

    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    public ManagementFilter(RequestMatcher requestMatcher){
        this.requestMatcher = requestMatcher;
        restAuthenticationEntryPoint=new RestAuthenticationEntryPoint();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        if(!requestMatcher.matches(req)){
            chain.doFilter(req,res);
            return;
        }

        boolean result = false;
        if(SecurityContextHolder.getContext()!=null
                && SecurityContextHolder.getContext().getAuthentication()!=null
                && SecurityContextHolder.getContext().getAuthentication().getAuthorities()!=null){
            Iterator<? extends GrantedAuthority> g = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator();
            if(g!=null){
                while(!result && g.hasNext()){
                    GrantedAuthority gg = g.next();
                    if(gg!=null && SpringSecurityConfig.PRIMARY_ACCOUNT.equals(gg.getAuthority())){
                        result = true;
                    }
                }
            }
        }
        if(result){
            chain.doFilter(req,res);
        }else{
            SecurityContextHolder.clearContext();
            restAuthenticationEntryPoint.commence(req, res, new DisabledException("不是PRIMARY_ACCOUNT"));
        }
    }
}
