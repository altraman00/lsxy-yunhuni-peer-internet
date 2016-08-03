package com.lsxy.app.portal.security;

import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.rest.UserRestToken;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by liups on 2016/6/21.
 * 自定义的校验过滤器
 */
public class RestAuthenticationProvider implements AuthenticationProvider, InitializingBean, MessageSourceAware {

    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    @Override
    public void afterPropertiesSet() throws Exception {
        //属性设置后的一些处理及检查
        Assert.notNull(this.messages, "A message source must be set");
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, authentication,
                messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.onlySupports",
                        "Only UsernamePasswordAuthenticationToken is supported"));

        // Determine username
        String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED"
                : authentication.getName();
        Object password = authentication.getCredentials();
        //RestApi登录
        String url = PortalConstants.REST_PREFIX_URL + "/login?username={1}&password={2}";
        String token;
        RestResponse<UserRestToken> response = RestRequest.buildRequest().get(url,UserRestToken.class,username,password);
        if(response.isSuccess()){
            token = response.getData().getToken();
        }else{
            throw new BadCredentialsException(response.getErrorMsg());
        }
        if (StringUtils.isEmpty(token)) {
            //这个类不要return null,以异常的形式处理结果
            throw new BadCredentialsException("密码错误,或账号被锁定");
        } else {
            return createSuccessAuthentication(username, authentication,roles("ROLE_TENANT_USER"),token);
        }

    }

    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, Collection<? extends GrantedAuthority> authorities,String tocken) {
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(
                principal, authentication.getCredentials(),authorities);
        result.setDetails(tocken);
        return result;
    }

    private List<GrantedAuthority> roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(
                roles.length);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority( role));
        }
        return authorities;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        //认证前的检查
        return (UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication));
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }
}
