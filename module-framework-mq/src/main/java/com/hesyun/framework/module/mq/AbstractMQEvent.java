package com.hesyun.framework.module.mq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.codec.binary.Base64;
import org.springframework.jms.core.MessageCreator;

import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.UUIDGenerator;

/**
 * mq消息对象
 * @author T420
 *
 */
public abstract class AbstractMQEvent implements MQEvent,MessageCreator,Serializable {
	
	private String id = UUIDGenerator.uuid();
	
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
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 对象从base64反向构建
	 * @param base64
	 * @return
	 */
	public static AbstractMQEvent buildFromBase64(String base64){
		byte[] bytes = Base64.decodeBase64(base64);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		AbstractMQEvent event = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object obj = ois.readObject();
			event = (AbstractMQEvent) obj;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return event;
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
