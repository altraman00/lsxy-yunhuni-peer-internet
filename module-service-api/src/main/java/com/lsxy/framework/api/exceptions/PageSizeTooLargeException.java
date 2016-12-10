package com.lsxy.framework.api.exceptions;

/**
 * Created by liups on 2016/11/18.
 */
public class PageSizeTooLargeException extends RuntimeException{
    public PageSizeTooLargeException(String message) {
        super(message);
    }
}
