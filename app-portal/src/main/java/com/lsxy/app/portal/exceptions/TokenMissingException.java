package com.lsxy.app.portal.exceptions;

/**
 * 当session中取不到连接restApi所要的token时，抛出此异常
 * Created by liups on 2016/6/28.
 */
public class TokenMissingException extends RuntimeException{
    public TokenMissingException(String message) {
        super(message);
    }
}
