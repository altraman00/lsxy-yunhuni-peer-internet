package com.lsxy.framework.mq.ons;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQProducer;
import com.lsxy.framework.mq.api.MQService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/21.
 */
@Component
@Conditional(OnsCondition.class)
public class OnsMQService implements MQService {

    public static final Logger logger = LoggerFactory.getLogger(OnsMQService.class);

    @Autowired
    private MQProducer mqProducer;

    @Override
    public void publishTopicEvent(MQEvent event) {

            if(event instanceof AbstractMQEvent){
                if (logger.isDebugEnabled()){
                    logger.debug("发布事件："+event.getEventName());
                    logger.debug("事件内容：{}" + event);
                 }

                mqProducer.publishEvent(event);
        }
    }
}
