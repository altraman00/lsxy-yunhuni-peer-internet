package com.lsxy.app.portal.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Tandy on 2016/6/7.
 */
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(SpringSecurityConfig.class);

    //不用授权就可以访问的资源
    private String [] permitUrls = new String[]{
            "/index",
            "/resources/**",  //静态资源
            "/login",
            "j_spring_security_check",
            "/vc/**"                //验证码
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("初始化Spring Security安全框架");
        }

        http.authorizeRequests()
                .antMatchers("/haha").access("hasRole('ROLE_USER')")
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?er=true")
//                .usernameParameter("username").passwordParameter("password")
                .and()
                .logout().logoutSuccessUrl("/login")
                .and()
                .csrf();

    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if(logger.isDebugEnabled()){
            logger.debug("初始化初始用户");
        }
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}
