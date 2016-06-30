package com.lsxy.app.api.gateway.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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
    public RestToken(String principal, RestCredentials credentials, Date timestamp, Collection authorities) {
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
    public final class RestCredentials {

        private String requestData;
        private String signature;

        public RestCredentials(String requestData, String signature) {
            this.requestData = requestData;
            this.signature = signature;
        }

        public String getRequestData() {
            return requestData;
        }

        public String getSignature() {
            return signature;
        }

    }
}




