package com.lsxy.app.backend;

import com.lsxy.framework.mq.api.MQConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

/**
 * 队列消息监听器
 * Created by liups on 2016/7/26.
 */
@Component
public class MQEventBackendListener {
    @Autowired
    private MQConsumer mqConsumer;

    @PostConstruct
    public void start() throws JMSException {
        mqConsumer.start();
    }

}
