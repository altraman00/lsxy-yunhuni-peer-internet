package com.lsxy.app.portal.rest.security;

import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.tenant.model.TenantRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tandy on 2016/6/7.
 */
@Service("userDetailsService")
public class PortalUserDetailsService implements UserDetailsService{

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //此处临时固定，后面调用jREST接口
        User user = new User("user",PasswordUtil.springSecurityPasswordEncode("password","user"),true,true,true,true,roles("ROLE_TENANT_USER"));
        return user;
    }

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
}
