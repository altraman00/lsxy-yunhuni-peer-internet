package com.lsxy.app.portal.security;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
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
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account;
        try {
            //根据登录用户名来查找用户
            account = accountService.findAccountByLoginName(username);
        } catch (MatchMutiEntitiesException e) {
            throw new RuntimeException("存在多个相同的用户名",e);
        }
        if(account == null){
            throw new UsernameNotFoundException("用户不存在");
        }else{
            User user = new User(account.getUserName(),account.getPassword(),true,true,true,true,roles("ROLE_TENANT_USER"));
            return user;
        }
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
