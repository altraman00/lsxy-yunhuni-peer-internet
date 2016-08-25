package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/23.
 */
public class IPNotInWhiteListException extends RuntimeException{
    public IPNotInWhiteListException(String message) {
        super(message);
    }
}
