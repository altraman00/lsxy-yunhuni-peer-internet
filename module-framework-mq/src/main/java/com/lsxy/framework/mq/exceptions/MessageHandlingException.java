package com.lsxy.framework.mq.exceptions;

import javax.jms.JMSException;

/**
 * Created by liups on 2016/7/26.
 */
public class MessageHandlingException extends Exception  {
    public MessageHandlingException(Exception e) {
        super(e);
    }
}
