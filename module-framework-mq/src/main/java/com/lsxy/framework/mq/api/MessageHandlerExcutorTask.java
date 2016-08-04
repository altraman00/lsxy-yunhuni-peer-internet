package com.lsxy.framework.mq.api;



import com.lsxy.framework.mq.exceptions.MessageHandlingException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by liups on 2016/7/26.
 *  异步执行处理消息任务
 */
@Component
public class MessageHandlerExcutorTask {
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
            e.printStackTrace();
            throw new MessageHandlingException(e);

        }
    }
}
