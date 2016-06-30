package com.lsxy.yuhuni.api.exceptions;

/**
 * Created by liups on 2016/6/30.
 */
public class SkChangeCountLimitException extends RuntimeException{
    public SkChangeCountLimitException(String message) {
        super(message);
    }
}
