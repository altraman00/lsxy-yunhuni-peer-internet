package com.lsxy.framework.mq.kafka;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.mq.api.AbstractMQProducer;
import com.lsxy.framework.mq.api.MQEvent;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Properties;

public class KafkaProducer extends AbstractMQProducer {

	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
	private Producer<String, MQEvent> producer;
	
	public void init(){
		logger.debug("try to build kafka produncer");
		Properties props = new Properties();
        props.put("metadata.broker.list", SystemConfig.getProperty("mq.kafka.metadata.broker.list","localhost:9092"));
        props.put("serializer.class", JsonEncoder.class.getName());
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        try{
        	producer = new Producer<String, MQEvent>(config);
        	logger.debug("kafka producer build successfull");
        }catch(Exception ex){
			logger.error("初始化MQ控件失败",ex);
        }
	}
	
	/**
	 * 发布事件
	 * @param event
	 */
	public void publishEvent(MQEvent event){
		Assert.notNull(producer,"kafka producer dis connected");
		String topic = event.getTopicName();
		KeyedMessage<String, MQEvent> data = new KeyedMessage<String, MQEvent>(topic, event);
		producer.send(data);
	}

	@Override
	public void destroy() {
		if(producer != null)
		producer.close();
		
	}

}
