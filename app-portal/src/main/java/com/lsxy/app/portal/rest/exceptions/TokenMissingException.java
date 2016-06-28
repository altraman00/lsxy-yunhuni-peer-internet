package com.lsxy.app.portal.rest.exceptions;

/**
 * Created by liups on 2016/6/28.
 */
public class TokenMissingException extends RuntimeException{
    public TokenMissingException(String message) {
        super(message);
    }
}
