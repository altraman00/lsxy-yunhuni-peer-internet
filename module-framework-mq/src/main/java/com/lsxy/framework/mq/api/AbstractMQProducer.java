package com.lsxy.framework.mq.api;

import com.lsxy.framework.mq.MQEvent;

public abstract class AbstractMQProducer {
	public abstract void init();
	public abstract void publishEvent(MQEvent event);
	public abstract void destroy();
	
}
