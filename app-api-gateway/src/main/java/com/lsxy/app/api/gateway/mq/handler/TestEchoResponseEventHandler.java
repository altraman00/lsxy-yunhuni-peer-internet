package com.lsxy.app.api.gateway.mq.handler;

import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.app.api.gateway.rest.AsyncRequestContext;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.apigw.APIGatewayResponseEvent;
import com.lsxy.framework.mq.events.apigw.test.TestEchoResponseEvent;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.jms.JMSException;

/**
 * Created by tandy on 16/8/7.
 * @see TestEchoResponseEvent
 */
@Component
public class TestEchoResponseEventHandler implements MQMessageHandler<TestEchoResponseEvent>{

    private static final Logger logger = LoggerFactory.getLogger(TestEchoResponseEventHandler.class);

    @Autowired
    private AsyncRequestContext requestContext;

    @Autowired
    private StasticsCounter sc;

    @Override
    public void handleMessage(TestEchoResponseEvent message) throws JMSException {
        String requestid = message.getRequestId();
        DeferredResult dr = requestContext.get(requestid);
        if(dr != null){
            dr.setResult(RestResponse.success(message.getMessage()));
           requestContext.remove(requestid);
        }
    }
}
