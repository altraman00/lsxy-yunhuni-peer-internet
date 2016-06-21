package com.lsxy.app.portal.security;

import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.tenant.model.TenantRole;
import com.lsxy.framework.web.rest.UserRestToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tandy on 2016/6/7.
 */
@Service("preUserDetailsService")
public class PreUserDetailsService implements AuthenticationUserDetailsService {


    private List<GrantedAuthority> buildUserAuthority(TenantRole... roles) {
        List<GrantedAuthority> Result = new ArrayList<>();
        for (TenantRole role : roles) {
            Result.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
         return Result;
    }


    private List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
                roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority( role));
        }
        return authorities;
    }

    @Override
    public UserDetails loadUserDetails(Authentication token) throws UsernameNotFoundException {
        String principal = (String) token.getPrincipal();
        User user = null;
        if(!StringUtils.isEmpty(principal)) {
            //此处应根据token从Redis获取用户并组装成UserDetails返回（principal为tocken） AbstractAuthenticationToken
            if("user-tocken".equals(principal)){
                user = new User("user",PasswordUtil.springSecurityPasswordEncode("password","user"),true,true,true,true,roles("ROLE_TENANT_USER"));
            }
        }
        if(user == null){
            throw new UsernameNotFoundException(principal);
        }
        return user;
    }
}
