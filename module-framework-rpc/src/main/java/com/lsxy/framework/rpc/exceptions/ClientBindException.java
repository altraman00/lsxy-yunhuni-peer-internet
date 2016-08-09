package com.lsxy.framework.rpc.exceptions;

/**
 * Created by tandy on 16/7/30.
 */
public class ClientBindException extends Exception {
    public ClientBindException(Exception e) {
        super(e);
    }
}
