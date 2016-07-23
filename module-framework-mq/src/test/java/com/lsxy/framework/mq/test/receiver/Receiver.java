package com.lsxy.framework.mq.test.receiver;

import com.lsxy.framework.mq.api.MQConsumer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.*;

/**
 * Created by Tandy on 2016/7/21.
 */
@Component
public class Receiver {

    @Autowired
    private MQConsumer mqConsumer;

    @PostConstruct
    public void start() throws JMSException {
        mqConsumer.start();
    }


//
////    @Bean
//        // Strictly speaking this bean is not necessary as boot creates a default
//    JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setPubSubDomain(true);
//        factory.setSubscriptionShared(true);
//        return factory;
//    }

//    @JmsListener(destination = "test_yunhuni_topic_framework_tenant", containerFactory = "myJmsContainerFactory")
    public void receiveMessage(String message) {
        System.out.println("=====hahhahahah======>" + message + ">");
    }

//
//    @PostConstruct
//    public void start() throws JMSException {
//        String brokerUrl = "tcp://localhost:61616";
//        String queueName = "test_yunhuni_topic_framework_tenant";
//        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
//        factory.setUserName("admin");
//        factory.setPassword("admin");
//        ActiveMQConnection connection = (ActiveMQConnection)factory.createConnection();
//        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        Destination topic = session.createTopic("test_yunhuni_topic_framework_tenant");
//        MessageConsumer consumer = session.createConsumer(topic);
//
//        consumer.setMessageListener(new MessageListener() {
//            @Override
//            public void onMessage(Message message) {
//                System.out.println("收到消息："+message);
//            }
//        });
//        connection.start();
//
//    }
}
