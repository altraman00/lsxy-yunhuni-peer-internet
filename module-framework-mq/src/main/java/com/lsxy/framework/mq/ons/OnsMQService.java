package com.lsxy.framework.mq.ons;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.AbstractMQService;
import com.lsxy.framework.mq.api.MQProducer;
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
public class OnsMQService extends AbstractMQService{

    public static final Logger logger = LoggerFactory.getLogger(OnsMQService.class);

    @Autowired
    private MQProducer mqProducer;

    @Override
    protected void publishEvent(AbstractMQEvent event) {
        long currenttime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("发布事件：{}",  event.getEventName());
            logger.debug("事件内容：{}" , event);
        }
        mqProducer.publishEvent(event);
        if(logger.isDebugEnabled()){
            logger.debug("事件发送完毕,花费{}ms",System.currentTimeMillis() - currenttime);
        }
    }
}
