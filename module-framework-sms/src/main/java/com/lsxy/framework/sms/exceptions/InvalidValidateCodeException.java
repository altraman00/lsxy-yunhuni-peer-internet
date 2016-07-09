package com.lsxy.framework.sms.exceptions;

/**
 * 验证码错误异常
 * Created by liups on 2016/7/7.
 */
public class InvalidValidateCodeException extends RuntimeException{
    public InvalidValidateCodeException(String message) {
        super(message);
    }
}
