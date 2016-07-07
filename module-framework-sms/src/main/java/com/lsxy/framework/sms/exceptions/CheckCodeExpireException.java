package com.lsxy.framework.sms.exceptions;

/**
 * 验证码过期异常
 * Created by liups on 2016/7/7.
 */
public class CheckCodeExpireException extends RuntimeException{
    public CheckCodeExpireException(String message) {
        super(message);
    }
}
