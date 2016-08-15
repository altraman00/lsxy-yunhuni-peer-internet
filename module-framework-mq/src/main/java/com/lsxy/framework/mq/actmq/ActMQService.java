package com.lsxy.framework.mq.actmq;

import com.lsxy.framework.mq.MQStasticCounter;
import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.AbstractMQService;
import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQService;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * Created by Tandy on 2016/7/21.
 */
@Component
@Conditional(ActMQCondition.class)
@DependsOn("jmsTemplate")
public class ActMQService extends AbstractMQService {

    public static final Logger logger = LoggerFactory.getLogger(ActMQService.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired(required = false)
    private MQStasticCounter sc;

    @Override
    protected void publishEvent(AbstractMQEvent event) {


        if(jmsTemplate != null){
            if(event instanceof AbstractMQEvent){
                if (logger.isDebugEnabled()){
                    logger.debug("发布事件：{}" , event.getEventName());
                    logger.debug("事件内容：{}" , event);
                }
                long starttime = System.currentTimeMillis();
                Destination dest = new ActiveMQTopic(event.getTopicName());
                jmsTemplate.send(dest,(AbstractMQEvent)event);
                if(logger.isDebugEnabled()){

                    logger.debug("事件发布完成: {}  花费:{}ms",event,System.currentTimeMillis() - starttime);
                }
                /*发送消息计数统计*/
                if(null != sc) sc.getSendMQCount().incrementAndGet();
            }
        }
    }

    @Override
    public boolean ready() {
        return false;
    }
}
