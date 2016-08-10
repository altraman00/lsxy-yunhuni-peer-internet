package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.test.TestEchoRequestEvent;
import com.lsxy.framework.mq.events.apigw.test.TestEchoResponseEvent;
import com.lsxy.framework.mq.events.apigw.test.TestResetStasticsCountEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Collection;

/**
 * Created by tandy on 16/8/10.
 */
@Component
@Profile(value={"local","development"})
public class TestEchoRequestEventHandler implements MQMessageHandler<TestEchoRequestEvent>  {

    private static final Logger logger = LoggerFactory.getLogger(TestEchoRequestEventHandler.class);

    @Autowired
    private MQService mqService;

    @Override
    public void handleMessage(TestEchoRequestEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("响应ECHO Request事件:{}",message);
        }

        TestEchoResponseEvent evt = new TestEchoResponseEvent(message.getRequestId(),message.getName());
        mqService.publish(evt);
    }
}
