package com.lsxy.app.portal.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.web.http.SessionRepositoryFilter;
import org.springframework.web.filter.CharacterEncodingFilter;

/**
 * Created by Tandy on 2016/6/7.
 */
@EnableWebSecurity(debug = false)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Log logger = LogFactory.getLog(SpringSecurityConfig.class);

    @Autowired
    private SessionRepositoryFilter springSessionRepositoryFilter;
    //暗码,配置成为生产环境为空的状态
    @Autowired(required = false)
    private String hideCode;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if(logger.isDebugEnabled()){
            logger.debug("初始化Spring Security安全框架");
        }
        String loginPage = "/login";
        String logoutPage = "/logout";
        String checkCodeFailurePage = "/login?er=vcer";
        String loginFailurePage = "/login?er=true";
        String loginSuccessPage = "/console/home";
        http.authorizeRequests()
                .antMatchers("/console/**").access("hasRole('ROLE_TENANT_USER')")
                .and()
                    //增加自定义的登录校验过滤器
                    .addFilterBefore(new CheckCodeAuthenticationFilter(loginPage,checkCodeFailurePage,hideCode),UsernamePasswordAuthenticationFilter.class)
                    .formLogin().loginPage(loginPage)
                    .failureUrl(loginFailurePage)
                    .defaultSuccessUrl(loginSuccessPage)
                    .successHandler(getPortalAuthenticationSuccessHandler())
                .and()
                    //默认的登陆只支持post请求，这里改变登出的路径匹配器，使其支持get请求
                    .logout().logoutRequestMatcher(new AntPathRequestMatcher(logoutPage)).logoutSuccessUrl(loginPage)
                .and()
                    .csrf().disable();
                    //登录登出不用csrf
//                    .ignoringAntMatchers(loginPage,logoutPage,"/test/upload/*");
//                .and()
//                    .exceptionHandling().accessDeniedPage("/exception/403");


        //CharacterEncodingFilter 过滤器如果碰到Security，必须添加在Security前面，否则会出现乱码问题
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        //将转码放在最前面 将springsession过滤器放最前面,转码必须在最前面
        http.addFilterBefore(springSessionRepositoryFilter, WebAsyncManagerIntegrationFilter.class);
        http.addFilterBefore(characterEncodingFilter,SessionRepositoryFilter.class);
    }

    @Bean
    protected AuthenticationSuccessHandler getPortalAuthenticationSuccessHandler(){
        return new PortalAuthenticationSuccessHandler();
    }


    /**
     * 配置加密机制,以及加密盐值
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        RestAuthenticationProvider provider = new RestAuthenticationProvider();

        auth.authenticationProvider(provider);

    }

}


