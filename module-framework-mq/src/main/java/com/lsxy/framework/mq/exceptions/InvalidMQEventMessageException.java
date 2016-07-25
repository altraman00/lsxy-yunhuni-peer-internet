package com.lsxy.framework.mq.exceptions;

/**
 * Created by Tandy on 2016/7/23.
 */
public class InvalidMQEventMessageException extends Exception {

    public InvalidMQEventMessageException(Throwable ex){
        super(ex);
    }
}
