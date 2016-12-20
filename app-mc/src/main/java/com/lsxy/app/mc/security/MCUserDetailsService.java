package com.lsxy.app.mc.security;

import com.lsxy.yunhuni.api.user.model.OcUser;
import com.lsxy.yunhuni.api.user.service.OcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tandy on 16/11/19.
 */
@Component
public class MCUserDetailsService implements UserDetailsService {

    @Autowired
    private OcUserService ocUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_MC"));
        OcUser user = ocUserService.findUserByLoginName(username);
        User userx = new User(username,user.getPassword(),true,true,true,true,authorities);
        return userx;
    }
}
