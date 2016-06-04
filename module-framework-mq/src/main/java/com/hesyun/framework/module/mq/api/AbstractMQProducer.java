package com.hesyun.framework.module.mq.api;

import com.hesyun.framework.module.mq.MQEvent;

public abstract class AbstractMQProducer {
	public abstract void init();
	public abstract void publishEvent(MQEvent event);
	public abstract void destroy();
	
}
