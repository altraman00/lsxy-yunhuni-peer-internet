package com.lsxy.app.api.gateway.security.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Date;

/**
 * Created by Tandy on 2016/6/30.
 */
public class RestToken extends UsernamePasswordAuthenticationToken {
    private Date timestamp;
    private String tenantId;
    private String subaccountId;

    // this constructor creates a non-authenticated token (see super-class)
    public RestToken(String principal, RestCredentials credentials, Date timestamp) {
        super(principal, credentials);
        this.timestamp = timestamp;
    }

    // this constructor creates an authenticated token (see super-class)
    public RestToken(String principal, RestCredentials credentials, Date timestamp,String tenantId,String subaccountId, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
        this.timestamp = timestamp;
        this.tenantId = tenantId;
        this.subaccountId = subaccountId;
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

    public String getTenantId() {
        return tenantId;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }
}




