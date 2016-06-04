package com.hesyun.framework.module.mq.ons;

import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.lsxy.framework.config.SystemConfig;
import com.hesyun.framework.module.mq.MQEvent;
import com.hesyun.framework.module.mq.api.AbstractMQProducer;

public class OnsProducer  extends AbstractMQProducer{
	private final static Log logger = LogFactory.getLog(OnsProducer.class);
	 private Producer producer;
	@Override
	public void publishEvent(MQEvent event) {
		Assert.notNull(event);
		Assert.notNull(event.toJson());
		
		byte[] messageBody = Base64.encodeBase64(event.toJson().getBytes());
		Properties p = new Properties();
		p.put("base64", true);
		Message msg = new Message( //
                // Message Topic
				event.getTopicName(),
                // Message Tag,
                // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在ONS服务器过滤
                event.getEventName(),
                // Message Body
                // 任何二进制形式的数据， ONS不做任何干预，
                // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                messageBody);
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过ONS Console查询消息并补发。
            // 注意：不设置也不会影响消息正常收发
            msg.setKey(event.getId());
            msg.setUserProperties(p);
            // 发送消息，只要不抛异常就是成功
            SendResult sendResult = producer.send(msg);
            logger.debug(sendResult);
	}
	
	public void init(){
		logger.debug("try to build ons producer");
		Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, SystemConfig.getProperty("mq.ons.pid","PID_TENANT_SERVICE"));
        properties.put(PropertyKeyConst.AccessKey,SystemConfig.getProperty("mq.ons.ak","3qPjLmZrmSgXHQKn"));
        properties.put(PropertyKeyConst.SecretKey, SystemConfig.getProperty("mq.ons.sk","CUB2Fl0NXtOnB5qfpNFOGf4VhVAte1"));
        logger.debug("ons properties:"+properties);
        producer = ONSFactory.createProducer(properties);
        logger.debug("ons producer build success,ready to start");
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();
        logger.debug("ons producer start success");
	}

	@Override
	public void destroy() {
		if(producer != null)
			producer.shutdown();
	}
	
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("a", "1");
		System.out.println(properties);
	}
}
