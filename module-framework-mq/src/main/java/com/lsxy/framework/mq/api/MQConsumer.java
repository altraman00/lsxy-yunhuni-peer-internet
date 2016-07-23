package com.lsxy.framework.mq.api;

import javax.jms.JMSException;

/**
 * Created by Tandy on 2016/7/22.
 */
public interface MQConsumer {

    public void init() throws JMSException;

    public void start() throws JMSException;

    public void destroy();

}
