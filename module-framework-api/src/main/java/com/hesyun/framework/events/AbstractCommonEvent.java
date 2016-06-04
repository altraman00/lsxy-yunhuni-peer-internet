package com.hesyun.framework.events;


import com.hesyun.framework.module.mq.AbstractMQEvent;

public class AbstractCommonEvent extends AbstractMQEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getTopicName() {
		return "com.lsxy.topic.framework.common";
	}

}
