package com.lsxy.framework.mq.api;

import javax.jms.JMSException;

import static javafx.scene.input.KeyCode.T;

/**
 * Created by Tandy on 2016/7/23.
 */
public interface MQMessageHandler<T extends MQEvent> {
    public abstract void handleMessage(T message) throws JMSException;
}
