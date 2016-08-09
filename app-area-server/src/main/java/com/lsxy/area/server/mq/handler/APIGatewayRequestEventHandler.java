package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.APIGatewayRequestEvent;
import com.lsxy.framework.mq.events.apigw.APIGatewayResponseEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.RequestWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by tandy on 16/8/8.
 */
@Component
public class APIGatewayRequestEventHandler implements MQMessageHandler<APIGatewayRequestEvent> {
    private static final Logger logger = LoggerFactory.getLogger(APIGatewayRequestEventHandler.class);

    @Autowired
    private RemoteServer remoteServer;

    @Autowired
    private ServerSessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private MQService mqService;

    @Override
    public void handleMessage(APIGatewayRequestEvent message) throws JMSException {

        if(logger.isDebugEnabled()) {
            logger.debug("处理APIGW请求:" + message);
        }

        //找到合适的区域代理
        Session session = sessionContext.getRightSession();
        APIGatewayResponseEvent evt = new APIGatewayResponseEvent(message.getRequestId());
        evt.setRequestTimestamp(message.getTimestamp());
        if(session != null){
            String callid = UUIDGenerator.uuid();
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL,"callid="+callid);
            try {
                rpcCaller.invoke(session,rpcrequest);
                evt.setSuccess(true);
                evt.setData("callid is : " + callid);
            } catch (RequestWriteException e) {
                e.printStackTrace();
                evt.setSuccess(false);
                evt.setData("消息发送到区域失败了:"+rpcrequest);
                logger.error("消息发送到区域失败:{}",rpcrequest);
            }

        }else{
            logger.error("没有找到合适的区域代理处理该请求:{}",message);

            evt.setSuccess(false);
            evt.setData("没有合适的区域代理处理您的请求");
        }
        mqService.publish(evt);
    }
}
