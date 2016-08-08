package com.lsxy.framework.mq.actmq;

import com.lsxy.framework.mq.api.AbstractMQConsumer;
import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MessageHandlerExcutorTask;
import com.lsxy.framework.mq.exceptions.InvalidMQEventMessageException;
import com.lsxy.framework.mq.exceptions.MessageHandlingException;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.util.Set;

/**
 * Created by Tandy on 2016/7/22.
 *
 */
@Conditional(ActMQCondition.class)
@Component
public class ActMQConsumer extends AbstractMQConsumer implements DisposableBean, InitializingBean,MessageListener {

    public static final Logger logger = LoggerFactory.getLogger(ActMQConsumer.class);
    
    private Session session;
    private Connection connection;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MessageHandlerExcutorTask messageHandlerExcutorTask;
    @Autowired
    private ConnectionFactory connectionFactory;



    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }


    @Override
    public void init() throws JMSException {
        connection = connectionFactory.createConnection();
    }

    @Override
    public void start() throws JMSException {
        this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        String topics[] = this.getTopics();
        for (String topicstr : topics) {
            Destination topic = session.createTopic(topicstr);
            MessageConsumer consumer = session.createConsumer(topic);
            consumer.setMessageListener(this);

            logger.info("消息订阅成功:{}",topicstr);
        }

        connection.start();

        logger.info("消息服务器启动成功");
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
            logger.debug("收到消息[原始]："+ message);
         }
        if(message instanceof  TextMessage){
            TextMessage tm = (TextMessage) message;
            try {
                String msg = tm.getText();

                if (logger.isDebugEnabled()){
                        logger.debug("收到消息[BSE64]:{}",msg);
                 }
                MQEvent event = this.parseMessage(msg);
                if (logger.isDebugEnabled()){
                        logger.debug("消息解析后："+event.getId());
                 }

                Set<Class<? extends MQMessageHandler>> handlers = this.getMqHandlerFactory().getHandler(event);
                for (Class hc: handlers) {
                    MQMessageHandler handler = (MQMessageHandler) applicationContext.getBean(hc);
                    messageHandlerExcutorTask.doTask(handler,event);
                }
            } catch (MessageHandlingException | JMSException | InvalidMQEventMessageException e) {
                logger.error("处理消息出现异常：{}",e);
                e.printStackTrace();
            }
        }
    }
}
