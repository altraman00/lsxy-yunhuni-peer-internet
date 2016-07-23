package com.lsxy.framework.mq.ons;

import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/21.
 */
@Component
@Conditional(OnsCondition.class)
public class AliOnsMQService implements MQService {

    public static final Logger logger = LoggerFactory.getLogger(AliOnsMQService.class);
//    @Autowired
//    private JmsTemplate jmsTemplate;

    @Override
    public void publishTopicEvent(MQEvent event) {

//        if(jmsTemplate != null){
//            if(event instanceof AbstractMQEvent){
//                if (logger.isDebugEnabled()){
//                    logger.debug("发布事件："+event.getEventName());
//                    logger.debug("事件内容：{}" + event);
//                 }
//                JmsBaseTopic dest = new JmsBaseTopic(event.getTopicName(),event.getEventName());
//
//                jmsTemplate.send(dest,(AbstractMQEvent)event);
//            }
//        }
    }
}
