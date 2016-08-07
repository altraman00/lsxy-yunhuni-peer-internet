package com.lsxy.app.api.gateway.mq.handler;

import com.lsxy.app.api.gateway.rest.AsyncRequestContext;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.rpc.APIGatewayResponseEvent;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import javax.jms.JMSException;

/**
 * Created by tandy on 16/8/7.
 * <p>用户在请求了接口后API网关会发出APIGatewayRequestEvent,与之匹配需要监听处理方的响应事件</p>
 * @see APIGatewayResponseEvent
 */
@Component
public class APIGatewayResponseEventHandler implements MQMessageHandler<APIGatewayResponseEvent>{

    @Autowired
    private AsyncRequestContext requestContext;

    @Override
    public void handleMessage(APIGatewayResponseEvent message) throws JMSException {
        String requestid = message.getHttpRequestId();
        DeferredResult dr = requestContext.get(requestid);
        if(dr != null){
            dr.setResult(RestResponse.success("成功返回的结果"));
           requestContext.remove(requestid);
        }
    }
}
