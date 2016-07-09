package com.lsxy.framework.sms.exceptions;

/**
 * 验证码生成过于频繁异常
 * Created by liups on 2016/7/7.
 */
public class TooManyGenTimesException extends RuntimeException{
    public TooManyGenTimesException(String message) {
        super(message);
    }
}
