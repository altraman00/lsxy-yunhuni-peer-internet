package com.lsxy.app.mc.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Created by Tandy on 2016/6/7.
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(SpringSecurityConfig.class);

    @Autowired
    private MCUserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("初始化Spring Security安全框架");
        }


        http.authorizeRequests()
                .antMatchers("/*").permitAll()
                .antMatchers("/admin/**").access("hasRole('MC')")
                .and().httpBasic()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        ;

    }



    /**
     * 配置校验器
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        ReflectionSaltSource rss = new ReflectionSaltSource();
        rss.setUserPropertyToUse("username");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setSaltSource(rss);

        provider.setUserDetailsService(myUserDetailsService);

        provider.setPasswordEncoder(getPasswordEncode());

        provider.setHideUserNotFoundExceptions(false);

        auth.authenticationProvider(provider);

    }

    /**
     * MD5加密器
     * @return
     */
    private org.springframework.security.authentication.encoding.PasswordEncoder getPasswordEncode() {
        ShaPasswordEncoder encode =  new org.springframework.security.authentication.encoding.ShaPasswordEncoder(256);
        encode.setEncodeHashAsBase64(true);
        return encode;
    }



}


