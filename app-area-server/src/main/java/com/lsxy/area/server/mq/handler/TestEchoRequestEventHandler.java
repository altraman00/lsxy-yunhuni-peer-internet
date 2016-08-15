package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.test.TestEchoRequestEvent;
import com.lsxy.framework.mq.events.apigw.test.TestEchoResponseEvent;
import com.lsxy.framework.mq.events.apigw.test.TestResetStasticsCountEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.HaveNoExpectedRPCResponseException;
import com.lsxy.framework.rpc.exceptions.RequestTimeOutException;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
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
@Profile(value={"test","local","development"})
public class TestEchoRequestEventHandler implements MQMessageHandler<TestEchoRequestEvent>  {

    private static final Logger logger = LoggerFactory.getLogger(TestEchoRequestEventHandler.class);

    @Autowired
    private MQService mqService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ServerSessionContext sessionContext;

    @Override
    public void handleMessage(TestEchoRequestEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("响应ECHO Request事件:{}",message);
        }

        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_TEST_ECHO,"param01=001");
        RPCResponse response = null;

        try {
            Session session = sessionContext.getRightSession();
            response = rpcCaller.invokeWithReturn(session,request);
            long currentTime = System.currentTimeMillis();
            logger.info("收到响应[{}],花费:{}ms   [{}]  vs [{}]",response,currentTime-response.getTimestamp(),response.getTimestamp(),currentTime);

            TestEchoResponseEvent responseMQEvent = new TestEchoResponseEvent(message.getRequestId(),request.getName());
            responseMQEvent.setRequestTimestamp(message.getTimestamp());
            mqService.publish(responseMQEvent);
        } catch (Exception e) {
            logger.error("响应ECHO Request事件出现异常:{}",message);
//            e.printStackTrace();
        }

    }
}
