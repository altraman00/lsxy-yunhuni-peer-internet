package com.lsxy.framework.mq.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import javax.jms.ConnectionFactory;

/**
 * Created by Tandy on 2016/7/21.
 */
//@Component
public class Receiver {


//    @Bean
        // Strictly speaking this bean is not necessary as boot creates a default
    JmsListenerContainerFactory<?> myJmsContainerFactory(ConnectionFactory connectionFactory) {
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setPubSubDomain(true);
        return factory;
    }

//    @JmsListener(destination = "test_yunhuni_topic_framework_tenant", containerFactory = "myJmsContainerFactory")
    public void receiveMessage(String message) {
        System.out.println("===========>" + message + ">");
    }
}
