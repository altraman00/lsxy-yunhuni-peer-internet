package com.lsxy.app.oc.security;

import com.lsxy.yunhuni.api.user.model.OcUser;
import com.lsxy.yunhuni.api.user.service.OcUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class APIUserDetailsService implements UserDetailsService{
    @Autowired
    OcUserService ocUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        OcUser ocUser;
        //根据登录用户名来查找用户
        ocUser = ocUserService.findUserByLoginName(username);
        if(ocUser == null){
            throw new UsernameNotFoundException("用户不存在");
        }else{
            User user = new User(ocUser.getUserName(),ocUser.getPassword(),true,true,true,true,roles("ROLE_OC_USER"));
            return user;
        }
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
