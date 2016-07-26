package com.lsxy.app.backend.handler;

import com.lsxy.framework.api.events.RegisterSuccessEvent;
import com.lsxy.framework.mq.api.MQMessageHandler;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by liups on 2016/7/26.
 */
@Component
public class RegisterSuccessEventHandler implements MQMessageHandler<RegisterSuccessEvent>{

    @Override
    public void handleMessage(RegisterSuccessEvent message) throws JMSException {

    }
}

