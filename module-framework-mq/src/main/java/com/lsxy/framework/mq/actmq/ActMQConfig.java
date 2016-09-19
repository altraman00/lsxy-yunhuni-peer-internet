package com.lsxy.framework.mq.actmq;

import com.lsxy.framework.config.SystemConfig;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

/**
 * Created by Tandy on 2016/7/21.
 * ActiveMQ连接配置
 */

@Component
@Configuration
@ConditionalOnProperty(value = "global.mq.provider", havingValue = "actmq", matchIfMissing = false)
public class ActMQConfig {


    @Bean
    public ConnectionFactory getConnectionFactory(){
//        mq.broker.url=failover:(tcp://v1mq01.hesyun.com:61616)
//        mq.topicName=hsy1.support.topic
//        mq.client.id=hsy1_support
//        mq.broker.userName=jmsbind
//        mq.broker.password=rg4yy2
//        mq.handlers.package=com.hesyun.app.support.mq.handles
        String brokerUrl = SystemConfig.getProperty("global.mq.activemq.url","failover:(tcp://localhost:61616)");
        String userName= SystemConfig.getProperty("global.mq.activemq.username","admin");
        String password = SystemConfig.getProperty("global.mq.activemq.password","admin");

        ActiveMQConnectionFactory amcf = new  ActiveMQConnectionFactory(userName,password,brokerUrl);
        CachingConnectionFactory cf = new CachingConnectionFactory(amcf);
//        cf.setClientId("test_client_001");
        return cf;
    }


}
