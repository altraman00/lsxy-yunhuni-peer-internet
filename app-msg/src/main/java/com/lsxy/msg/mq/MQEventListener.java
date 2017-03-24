package com.lsxy.msg.mq;

import com.lsxy.framework.mq.api.MQConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;

/**
 * Created by tandy on 16/8/7.
 */
@Component
public class MQEventListener {

    private static final Logger logger = LoggerFactory.getLogger(MQEventListener.class);

    @Autowired
    private MQConsumer mqConsumer;

    @PostConstruct
    public void startmq(){
        try {
            mqConsumer.start();
        } catch (JMSException e) {
            logger.error("初始化mq组件失败:{}",e.getMessage());
        }
    }

}
