package com.lsxy.app.portal.security;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tandy on 2016/6/7.
 */
@Service("preUserDetailsService")
public class PreUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    private RedisCacheService cacheManager;

    private List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority( role));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {
        String principal = (String) token.getPrincipal();
        User user = null;
        if(!StringUtils.isEmpty(principal)) {
            //此处应根据token从Redis获取用户并组装成UserDetails返回（principal为tocken） AbstractAuthenticationToken
            String username = cacheManager.get(principal);
            if(!StringUtils.isEmpty(username)){
                cacheManager.expire(principal,30*60);
                user = new User(username,"",true,true,true,true,roles("ROLE_TENANT_USER"));
            }
        }
        if(user == null){
            throw new UsernameNotFoundException(principal);
        }
        return user;
    }
}
