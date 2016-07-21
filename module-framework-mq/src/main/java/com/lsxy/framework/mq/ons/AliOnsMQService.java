package com.lsxy.framework.mq.ons;

import com.aliyun.openservices.ons.jms.domain.JmsBaseTopic;
import com.lsxy.framework.mq.api.AbstractMQEvent;
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

import static com.lsxy.framework.cache.FrameworkCacheConfig.logger;

/**
 * Created by Tandy on 2016/7/21.
 */
@Component
@Conditional(OnsCondition.class)
public class AliOnsMQService implements MQService {

    public static final Logger logger = LoggerFactory.getLogger(AliOnsMQService.class);
    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    public void publishTopicEvent(MQEvent event) {

        if(jmsTemplate != null){
            if(event instanceof AbstractMQEvent){
                if (logger.isDebugEnabled()){
                    logger.debug("发布事件："+event.getEventName());
                    logger.debug("事件内容：{}" + event);
                 }
                JmsBaseTopic dest = new JmsBaseTopic(event.getTopicName(),event.getEventName());
                
                jmsTemplate.send(dest,(AbstractMQEvent)event);
            }
        }
    }
}
