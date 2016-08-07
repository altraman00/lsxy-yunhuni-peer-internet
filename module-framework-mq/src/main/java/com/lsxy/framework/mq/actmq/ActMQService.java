package com.lsxy.framework.mq.actmq;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.AbstractMQService;
import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQService;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * Created by Tandy on 2016/7/21.
 */
@Component
@Conditional(ActMQCondition.class)
public class ActMQService extends AbstractMQService {

    public static final Logger logger = LoggerFactory.getLogger(ActMQService.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    protected void publishEvent(AbstractMQEvent event) {
        if(jmsTemplate != null){
            if(event instanceof AbstractMQEvent){
                if (logger.isDebugEnabled()){
                    logger.debug("发布事件：{}" , event.getEventName());
                    logger.debug("事件内容：{}" , event);
                }
                Destination dest = new ActiveMQTopic(event.getTopicName());
                jmsTemplate.send(dest,(AbstractMQEvent)event);
            }
        }
    }
}
