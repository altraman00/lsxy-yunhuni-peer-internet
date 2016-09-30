package com.lsxy.framework.mq.api;



import com.lsxy.framework.mq.exceptions.MessageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by liups on 2016/7/26.
 *  异步执行处理消息任务
 */
@Component
public class MessageHandlerExcutorTask {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlerExcutorTask.class);
    /**
     *异步执行任务
     * @param handler
     * @param event
     * @throws MessageHandlingException
     */
    @Async
    public void doTask(MQMessageHandler handler,MQEvent event) throws MessageHandlingException{
        try {
            handler.handleMessage(event);
        } catch (JMSException e) {
            logger.error("JMS异常",e);
            throw new MessageHandlingException(e);

        }
    }
}
