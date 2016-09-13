package com.lsxy.framework.mq.api;

import org.apache.activemq.ScheduledMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * mq消息对象
 * @author T420
 *
 */
public abstract class AbstractDelayMQEvent extends AbstractMQEvent{

	/**
	 * 延时毫秒
	 */
	private Integer delay;

	public AbstractDelayMQEvent(){

	}

	public AbstractDelayMQEvent(Integer delay){
		this.delay = delay;
	}

	public Integer getDelay() {
		return delay;
	}

	public void setDelay(Integer delay) {
		this.delay = delay;
	}

	public boolean isDelay(){
		return this.delay != null && this.delay>0;
	}

	@Override
	public Message createMessage(Session session) throws JMSException {
		String uuid =  this.getId();
		String message = this.buildMessage();
		Message txtMessage = session.createTextMessage(message);
		txtMessage.setStringProperty("eventName", this.getEventName());
		txtMessage.setStringProperty("uuid", uuid);
		if(this.isDelay()){
			txtMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,delay);
		}
		return txtMessage;
	}
}
