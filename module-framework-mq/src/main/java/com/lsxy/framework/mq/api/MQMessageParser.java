package com.lsxy.framework.mq.api;

import com.lsxy.framework.mq.exceptions.InvalidMQEventMessageException;

/**
 * Created by Tandy on 2016/7/23.
 */
public interface MQMessageParser {
    public MQEvent parse(String message) throws InvalidMQEventMessageException;
}
