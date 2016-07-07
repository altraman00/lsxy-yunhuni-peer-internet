package com.lsxy.app.api.gateway.security;

import com.lsxy.app.api.gateway.security.auth.RestAuthenticationProvider;
import com.lsxy.app.api.gateway.security.auth.SignatureAuthFilter;
import com.lsxy.app.api.gateway.security.ip.BlackIpListFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.apache.zookeeper.ZooDefs.OpCode.auth;

/**
 * Created by Tandy on 2016/6/7.
 */
@EnableWebSecurity
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(SpringSecurityConfig.class);


    @Autowired
    private RestAuthenticationProvider restAuthenticationProvider;

    @Autowired
    private SignatureAuthFilter signatureAuthFilter;

    @Autowired
    private BlackIpListFilter blackIpListFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("初始化Spring Security安全框架");
        }

        http.authorizeRequests()
                .antMatchers("/**").permitAll()
                .and().httpBasic()
                .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
        ;

        http.addFilterBefore(signatureAuthFilter, BasicAuthenticationFilter.class);
        http.addFilterBefore(blackIpListFilter,SignatureAuthFilter.class);

    }



    @Bean
    public AuthenticationManager getAuthenticationManager() throws Exception {
        return this.authenticationManager();
    }


    /**
     * 配置校验器
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user001").password("123").roles("TENANT_USER");
        auth.authenticationProvider(restAuthenticationProvider);
    }



}


