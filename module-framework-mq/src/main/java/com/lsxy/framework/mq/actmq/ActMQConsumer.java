package com.lsxy.framework.mq.actmq;

import com.lsxy.framework.mq.api.AbstractMQConsumer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.jms.*;

/**
 * Created by Tandy on 2016/7/22.
 *
 */
@Conditional(ActMQCondition.class)
@Component
public class ActMQConsumer extends AbstractMQConsumer implements DisposableBean, InitializingBean,MessageListener {

    public static final Logger logger = LoggerFactory.getLogger(ActMQConsumer.class);
    
    private Session session;
    private ActiveMQConnection connection;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }


    @Override
    public void init() throws JMSException {
        String brokerUrl = "tcp://localhost:61616";
        String queueName = "test_yunhuni_topic_framework_tenant";
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);
        factory.setUserName("admin");
        factory.setPassword("admin");
        connection = (ActiveMQConnection)factory.createConnection();
    }

    @Override
    public void start() throws JMSException {
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        String topics[] = this.getTopics();
        for (String topicstr : topics) {
            Destination topic = session.createTopic(topicstr);
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(this);
        }
        connection.start();
    }

    @Override
    public void destroy() {
        if(connection != null){
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onMessage(Message message) {
        if (logger.isDebugEnabled()){
                logger.debug("收到消息："+ message);
         }
    }
}
