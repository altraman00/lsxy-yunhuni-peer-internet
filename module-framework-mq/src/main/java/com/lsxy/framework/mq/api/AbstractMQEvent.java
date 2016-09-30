package com.lsxy.framework.mq.api;

import com.lsxy.framework.core.utils.JSONUtil2;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * mq消息对象
 * @author T420
 *
 */
public abstract class AbstractMQEvent implements MQEvent,MessageCreator,Serializable {
	public static final Logger logger = LoggerFactory.getLogger(AbstractMQEvent.class);
	private String id;

	private  long timestamp;

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 事件topicName
	 * 组成规则：
	 * hsy.事件分类.事件类名
	 * @return
	 */
	@Override
	public String getEventName(){
		String clazz = this.getClass().getName();
		return clazz;
	}
	
//	private static Log logger = LogFactory.getLog(MQEvent.class);
	
	@Override
	public Message createMessage(Session session) throws JMSException {
		String uuid =  this.getId();
		String message = this.buildMessage();
		Message txtMessage = session.createTextMessage(message);
		txtMessage.setStringProperty("eventName", this.getEventName());
		txtMessage.setStringProperty("uuid", uuid);
		return txtMessage;
	}

	public String buildMessage(){
		return this.toBase64Text();
	}
	
	
	
	/**
	 * 当前对象序列化成base64
	 * @return
	 */
	public String toBase64Text(){
		String result = null;
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bao);
			oos.writeObject(this);
			byte[] bytes = bao.toByteArray();
			result = Base64.encodeBase64String(bytes);
		} catch (IOException e) {
			logger.error("IO异常",e);
		}
		return result;
	}

	/**
	 * 输出消息体
	 */
	@Override
	public String toString() {
		return JSONUtil2.objectToJson(this,false);
	}

	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public String toJson() {
		return JSONUtil2.objectToJson(this,false);
	}


}
