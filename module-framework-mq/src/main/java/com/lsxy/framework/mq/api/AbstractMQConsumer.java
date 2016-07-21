package com.lsxy.framework.mq.api;

import com.lsxy.framework.mq.MQEvent;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;


@SuppressWarnings("rawtypes")
public abstract class AbstractMQConsumer {

	private static final Log logger = LogFactory.getLog(AbstractMQConsumer.class);
	@Autowired
	private GlobalEventHandlerFactory globalEventHandlerFactory;

	public abstract void init();

	public abstract void start();

	public abstract void destroy();

	public abstract void await();


	public String[] getTopics(){
		String t = SystemConfig.getProperty("mq.subscribe.topics");
		String[] topics = null;
		if(StringUtil.isNotEmpty(t)){
			topics = t.split(",");
		}
		return topics;
	}
	
	/**
	 * 解析消息对象
	 * @param msg
	 * @return
	 */
	
	public MQEvent parseMessage(String msg) {
		JSONObject jsonObject = null;
		try{
			jsonObject = JSONObject.fromObject(msg);
		}catch(Exception ex){
			return null;
		}
		Assert.notNull(jsonObject,"无效的json 消息字符串");
		String eventClassName = jsonObject.getString("eventName"); 
		Assert.notNull(eventClassName,"无效的事件消息体："+msg);
		MQEvent result = null;
		try {
			logger.debug("实例化事件对象："+eventClassName);
			Class clazz = Class.forName(eventClassName);
			Object obj = JSONObject.toBean(jsonObject, clazz);
			logger.debug("事件对象赋值："+jsonObject);
			
			if(obj instanceof MQEvent){
				result = (MQEvent) obj;
				logger.debug("解析出事件类:"+result.getEventName());
			}
		} catch (ClassNotFoundException | SecurityException e) {
			e.printStackTrace();
		}
		return result;
	}

	public GlobalEventHandlerFactory getGlobalEventHandlerFactory() {
		return globalEventHandlerFactory;
	}

	public void setGlobalEventHandlerFactory(
			GlobalEventHandlerFactory globalEventHandlerFactory) {
		this.globalEventHandlerFactory = globalEventHandlerFactory;
	}
	
	
}
