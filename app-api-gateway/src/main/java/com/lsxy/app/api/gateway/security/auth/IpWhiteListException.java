package com.lsxy.app.api.gateway.security.auth;

/**
 * Created by liups on 2017/3/28.
 */
public class IpWhiteListException extends RuntimeException {
    public IpWhiteListException(String msg, Throwable t) {
        super(msg, t);
    }

    public IpWhiteListException(String msg) {
        super(msg);
    }
}
