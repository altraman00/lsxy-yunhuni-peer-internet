package com.lsxy.framework.sms.exceptions;

/**
 * 验证码检查次数达到上限异常
 * Created by liups on 2016/7/7.
 */
public class CheckOutMaxTimesException extends RuntimeException{
    public CheckOutMaxTimesException(String message) {
        super(message);
    }
}
