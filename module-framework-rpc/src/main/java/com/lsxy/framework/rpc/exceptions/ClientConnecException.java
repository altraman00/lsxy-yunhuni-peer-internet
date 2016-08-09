package com.lsxy.framework.rpc.exceptions;

/**
 * Created by tandy on 16/8/3.
 */
public class ClientConnecException extends Exception {
    public ClientConnecException(Exception ex) {
        super(ex);
    }

    public ClientConnecException(String s) {
        super(s);
    }
}
