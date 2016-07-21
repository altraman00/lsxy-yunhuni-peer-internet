package com.lsxy.framework.mq.api;


public interface GlobalEventHandler<T extends MQEvent> {
	
	public void handle(T msg);

}
