package com.lsxy.app.backend.handler;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.portal.ModifyEmailSuccessEvent;
import com.lsxy.framework.mq.events.test.EchoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by tandy on 16/9/27.
 */
@Component
public class EchoEventHandler implements MQMessageHandler<EchoEvent> {
    private static final Logger logger = LoggerFactory.getLogger(EchoEventHandler.class);

    @Autowired
    private RedisCacheService cacheService;

    @Override
    public void handleMessage(EchoEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("收到测试消息:{}" , message);
        }
        cacheService.incr("echo");
        cacheService.expire("echo",60*60*1000);
    }
}
