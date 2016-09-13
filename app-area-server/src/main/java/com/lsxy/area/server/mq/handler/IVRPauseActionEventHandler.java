package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.mq.api.MQMessageHandler;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by liuws on 2016/9/13.
 */
@Component
public class IVRPauseActionEventHandler implements MQMessageHandler<IVRPauseActionEvent> {


    @Override
    public void handleMessage(IVRPauseActionEvent message) throws JMSException {
//        message
    }
}
