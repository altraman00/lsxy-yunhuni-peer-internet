package com.lsxy.framework.mq.api;

import com.lsxy.framework.mq.MQEvent;


public interface GlobalEventHandler<T extends MQEvent> {
	
	public void handle(T msg);

}
