package com.lsxy.framework.mq.api;

public class MQManager {
	
//	private static final Log logger = LogFactory.getLog(MQManager.class);
//
//	private JmsTemplate jmsTemplate;
//
//	private AbstractMQProducer mqProducer;
//
//
//	public AbstractMQProducer getMqProducer() {
//		return mqProducer;
//	}
//
//	public void setMqProducer(AbstractMQProducer mqProducer) {
//		this.mqProducer = mqProducer;
//	}
//
//	/**
//	 * 向mq server发布一个事件主题
//	 * 如果是全局事件，就发到kafka，否则丢到activemq
//	 */
//	public void publishTopicEvent(MQEvent event){
//		//如果拥有全局事件注解，就发布全局事件
//		if(event.getClass().isAnnotationPresent(GlobalEvent.class)){
//			publishGlobalEvent(event);
//		}else{
//			if(jmsTemplate != null){
//				if(event instanceof AbstractMQEvent){
//					logger.debug("发布事件："+event.getEventName()+"\r\n"+event);
//					Destination dest = new  ActiveMQTopic(event.getTopicName());
//					jmsTemplate.send(dest,(AbstractMQEvent)event);
//				}
//			}
//		}
//	}
//
//	public JmsTemplate getJmsTemplate() {
//		return jmsTemplate;
//	}
//
//	public void setJmsTemplate(JmsTemplate jmsTemplate) {
//		this.jmsTemplate = jmsTemplate;
//	}
//
//	public void publishGlobalEvent(MQEvent event){
//		logger.debug("publishGE:"+event);
//		mqProducer.publishEvent(event);
//	}
	
	
}
