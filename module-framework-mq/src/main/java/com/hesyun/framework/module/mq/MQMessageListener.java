package com.hesyun.framework.module.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheManager;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;

/**
 * 消息监听 监听到的消息调用对应的handle进行处理 handle类名符合以下命名规则才能找到对应的handle类
 * com.hesyun.web.portal.mq.handles.[topicName的第二部分].[topicName中的事件名称部分]Handle
 *
 */
@Component("mqMessageListener")
@SuppressWarnings("unchecked") 
public class MQMessageListener implements MessageListener {

	private static Log logger = LogFactory.getLog(MQMessageListener.class);

	@Autowired
	private RedisCacheManager redisCacheManager;
	
	@Autowired
	private MQHandlerFactory mqHandlerFactory;
	
	@Override
	@Transactional
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			try {
				String base64 = tm.getText();
				AbstractMQEvent evt = AbstractMQEvent.buildFromBase64(base64);
				
				if(evt == null){
					logger.error("MQ消息体解析失败："+base64);
					return;
				}
				
				AbstractMessageHandle<AbstractMQEvent> handler = mqHandlerFactory.getHandler(evt.getClass());
				if(handler != null){
					String topicName = evt.getEventName();
					String uuid = tm.getStringProperty("uuid");
					logger.debug("["+uuid+"]接收到事件消息：" + evt.getEventName()+"\r\n"+evt);
					String sysid = SystemConfig.getProperty("system.id");
					String cacheKey = "mq_event_"+uuid;
					//互斥处理topic，但是可以排除指定的topic，排除的topic将会在每一个节点中调用
					String mutexExcludes = SystemConfig.getProperty("mq.mutex.excludes","");
					//如果topic在互斥处理的排除里面的话，所有节点马上处理该消息
					if(mutexExcludes.indexOf(topicName)>=0){
						logger.debug("["+uuid+"]马上处理该消息："+uuid);
						handler.handleMessage(evt);
						return;
					}
					//执行互斥处理消息
					String flagValue = redisCacheManager.get(cacheKey);
					if(StringUtil.isNotEmpty(flagValue)){
						logger.debug("["+uuid+"]缓存中已被设置标记，该消息被"+flagValue+"处理了");
					}else{
						try {
							logger.debug("["+uuid+"]准备处理该消息:"+uuid);
							redisCacheManager.setTransactionFlag(cacheKey,sysid);
							String currentCacheValue = redisCacheManager.get(cacheKey);
							logger.debug("["+uuid+"]当前cacheValue:"+currentCacheValue);
							if(currentCacheValue.equals(sysid)){
								logger.debug("["+uuid+"]马上处理该消息："+uuid);
								handler.handleMessage(evt);
							}else{
								logger.debug("["+uuid+"]标记位不一致"+currentCacheValue+"  vs "+ sysid);
							}
						} catch (TransactionExecFailedException e) {
							String tmp = redisCacheManager.get("mq_event__"+uuid);
							logger.debug("["+uuid+"]设置标记位异常了，该消息被"+tmp+"处理了");
						}
					}
				}else{
//					String topicName = evt.getEventName(); 
//					logger.debug("没有处理事件：" + topicName);
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

}
