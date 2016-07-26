package com.lsxy.framework.mq.test.receiver;

import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.test.events.TestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by Tandy on 2016/7/23.
 */
@Component
public class TestEventHandler implements MQMessageHandler<TestEvent> {
    public static final Logger logger = LoggerFactory.getLogger(TestEventHandler.class);

    @Override
    public void handleMessage(TestEvent message) throws JMSException {
            long offset = (System.currentTimeMillis() - message.getTimestamp());
            logger.info("处理消息[花费{}ms]：{}" , offset,message);
    }
}
