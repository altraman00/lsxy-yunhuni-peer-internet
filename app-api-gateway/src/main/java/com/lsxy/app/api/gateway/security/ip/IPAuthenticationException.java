package com.lsxy.app.api.gateway.security.ip;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by Tandy on 2016/7/7.
 * IP黑名单认证异常
 */
public class IPAuthenticationException extends AuthenticationException {
    public IPAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public IPAuthenticationException(String msg) {
        super(msg);
    }
}
