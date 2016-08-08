package com.lsxy.framework.mq.ons;

import com.aliyun.openservices.ons.api.*;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.mq.api.AbstractMQConsumer;
import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MessageHandlerExcutorTask;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Set;

@SuppressWarnings({"rawtypes","unchecked"})

@Component
@Lazy(value = true)
@Conditional(OnsCondition.class)
public class OnsConsumer extends AbstractMQConsumer implements MessageListener,InitializingBean,DisposableBean{

	private static final Log logger = LogFactory.getLog(OnsConsumer.class);
	private Consumer consumer;

	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private MessageHandlerExcutorTask messageHandlerExcutorTask;
	@Autowired
	private OnsMQConfig onsMQConfig;

	private String[] topics = null;
	
	@Override
	public void init() {
		logger.debug("try to build ons consumer");
		try{
			this.topics = getTopics();
			if(this.topics == null || this.topics.length <= 0){
				logger.debug("未配置mq.subscribe.topics项，不启动订阅服务");
				return;
			}
			
//			Properties properties = new Properties();
//			properties.put(PropertyKeyConst.ConsumerId, SystemConfig.getProperty("global.mq.ons.cid","CID_YUNHUNI-TENANT-001"));
//			properties.put(PropertyKeyConst.AccessKey, SystemConfig.getProperty("global.mq.ons.ak","nfgEUCKyOdVMVbqQ"));
//			properties.put(PropertyKeyConst.SecretKey, SystemConfig.getProperty("global.mq.ons.sk","HhmxAMZ2jCrE0fTa2kh9CLXF9JPcOW"));

			Properties properties = onsMQConfig.getOnsProperties();

			consumer = ONSFactory.createConsumer(properties);
			logger.debug("ons consumer build success,ready to start");

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	@Override
	public void start() {
		for (String topic : topics) {
			logger.debug("ons subscribe topc:"+topic);
			consumer.subscribe(topic, "*",this);	
		}
		consumer.start();
		logger.debug("ons consumer start successfully ");
	}

	@Override
	public void destroy() {
		consumer.shutdown();
	}

//	@Override
//	public void await() {
//		while(consumer.isStarted()){
//			try {
//				TimeUnit.SECONDS.sleep(10);
//				logger.debug("-------------------------");
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	
	public Action consume(Message message, ConsumeContext context) {
		String msg = null;
		try {
			logger.debug("recivied a message with id:["+message.getMsgID()+"] and message key is :["+message.getKey()+"]");
			logger.debug("msg user properties:"+message.getUserProperties());
			String isBase64Encode = message.getUserProperties("base64");
			if(isBase64Encode != null && isBase64Encode.equals("true")){
				msg = new String(Base64.decodeBase64(message.getBody()),"UTF-8");
			}else{
				msg = new String(message.getBody(),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(msg == null)
			return null;
		logger.debug("recivied msg :" + msg);
		try {
			MQEvent event = parseMessage(msg);
			if (event != null) {
				logger.debug("parse msg to MQEvent object and id is :"	+ event.getId());
				Set<Class<? extends MQMessageHandler>> handlers = this.getMqHandlerFactory().getHandler(event);
				for (Class hc: handlers) {
					MQMessageHandler handler = (MQMessageHandler) applicationContext.getBean(hc);
					messageHandlerExcutorTask.doTask(handler,event);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return Action.CommitMessage;
	}


	@Override
	public void afterPropertiesSet() throws Exception {
		this.init();
	}

	public static void main(String[] args) {
		OnsConsumer consumer = new OnsConsumer();
		consumer.init();
		consumer.start();
	}
}
