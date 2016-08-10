package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.APIGatewayRequestEvent;
import com.lsxy.framework.mq.events.apigw.APIGatewayResponseEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.web.utils.WebUtils;
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

    @Autowired(required = false)
    private StasticsCounter cs;

    @Autowired
    private ServerSessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private MQService mqService;

    @Override
    public void handleMessage(APIGatewayRequestEvent message) throws JMSException {

        /*请求计数器*/
        if(cs!=null) cs.getReceivedGWRequestCount().incrementAndGet();

        if(logger.isDebugEnabled()) {
            logger.debug("处理APIGW请求:" + message);
        }
        APIGatewayResponseEvent evt = new APIGatewayResponseEvent(message.getRequestId());
        evt.setRequestTimestamp(message.getTimestamp());

        //找到合适的区域代理
        Session session = sessionContext.getRightSession();

        if(session != null){
            RPCRequest rpcrequest = null;
            //分解动作  sys.call
            String method = message.getMethod();
            if(method.equals("sys.call")){
                String callid = UUIDGenerator.uuid();
                message.getParams().put("callid",callid);
                String params = WebUtils.paramsMapToQueryString(message.getParams());
                rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL,params);
                /*呼叫API调用次数计数*/
                cs.getSendAreaNodeSysCallCount().incrementAndGet();
            }

            if(rpcrequest != null){
                try {
                    if(logger.isDebugEnabled()){
                        logger.debug("发送SYS_CALL指令到区域:{}",rpcrequest);
                    }

                    /*发送给区域的请求次数计数*/
                    if(cs!=null) cs.getSendAreaNodeRequestCount().incrementAndGet();

                    rpcCaller.invoke(session,rpcrequest);
                    evt.setSuccess(true);
                } catch (Exception e) {
                    evt.setSuccess(false);
                    logger.error("消息发送到区域失败:{}",rpcrequest);
                }
            }else{
                if(logger.isDebugEnabled()){
                    logger.debug("没有处理网关请求:{}",message);
                }
            }
        }else{
            logger.error("没有找到合适的区域代理处理该请求:{}",message);

            evt.setSuccess(false);
            evt.setData("没有合适的区域代理处理您的请求");
        }

        /*给API网关的响应计数*/
        if(cs!=null) cs.getSendGWResponseCount().incrementAndGet();
        mqService.publish(evt);
    }
}
