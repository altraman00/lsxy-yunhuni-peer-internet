package com.lsxy.app.api.gateway.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Tandy on 2016/6/30.
 */
public class RestToken extends UsernamePasswordAuthenticationToken {
    private Date timestamp;

    // this constructor creates a non-authenticated token (see super-class)
    public RestToken(String principal, RestCredentials credentials, Date timestamp) {
        super(principal, credentials);
        this.timestamp = timestamp;
    }

    // this constructor creates an authenticated token (see super-class)
    public RestToken(String principal, RestCredentials credentials, Date timestamp, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.timestamp = timestamp;
    }

    @Override
    public String getPrincipal() {
        return (String) super.getPrincipal();
    }

    @Override
    public RestCredentials getCredentials() {
        return (RestCredentials) super.getCredentials();
    }

    public Date getTimestamp() {
        return timestamp;
    }

}




