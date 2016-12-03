package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.agentserver.IVRPauseActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * 处理IVR暂停指令消息
 * Created by liuws on 2016/9/13.
 */
@Component
public class IVRPauseActionEventHandler implements MQMessageHandler<IVRPauseActionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(IVRPauseActionEventHandler.class);

    @Autowired
    private IVRActionService ivrActionService;

    @Override
    public void handleMessage(IVRPauseActionEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理IVR暂停动作{}",message.toJson());
        }
        ivrActionService.doAction(message.getCallId(),null);
    }
}
