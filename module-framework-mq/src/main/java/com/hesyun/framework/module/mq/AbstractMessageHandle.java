package com.hesyun.framework.module.mq;

import javax.jms.JMSException;


public abstract class AbstractMessageHandle<T extends AbstractMQEvent> implements MessageHandle {
	public abstract void handleMessage(T message) throws JMSException;
}
