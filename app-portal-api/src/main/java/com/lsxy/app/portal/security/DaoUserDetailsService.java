package com.lsxy.app.portal.security;

import com.lsxy.framework.core.utils.PasswordUtil;
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
 * Created by liups on 2016/6/21.
 */
@Service("daoUserDetailsService")
public class DaoUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //此处临时固定，后面调用数据库查询
        User user = new User("user",PasswordUtil.springSecurityPasswordEncode("password","user"),true,true,true,true,roles("ROLE_TENANT_USER"));
        return user;
    }


    private List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority( role));
        }
        return authorities;
    }
}
