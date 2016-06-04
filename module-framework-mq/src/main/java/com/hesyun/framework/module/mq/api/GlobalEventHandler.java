package com.hesyun.framework.module.mq.api;

import com.hesyun.framework.module.mq.MQEvent;



public interface GlobalEventHandler<T extends MQEvent> {
	
	public void handle(T msg);

}
