package com.lsxy.framework.sms.exceptions;

/**
 * 验证码不存在或已超时
 * Created by liups on 2016/7/7.
 */
public class CheckCodeNotFoundException extends RuntimeException{
    public CheckCodeNotFoundException(String message) {
        super(message);
    }
}
