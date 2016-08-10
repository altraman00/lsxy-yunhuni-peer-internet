package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.apigw.APIGatewayRequestEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.RequestWriteException;
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
@Profile(value={"local","development"})
public class TestResetStasticsCountEventHandler implements MQMessageHandler<APIGatewayRequestEvent>  {

    private static final Logger logger = LoggerFactory.getLogger(TestResetStasticsCountEventHandler.class);

    @Autowired
    private StasticsCounter sc;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ServerSessionContext sessionContext;

    @Override
    public void handleMessage(APIGatewayRequestEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("重置统计数据");
        }
        sc.reset();
        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_TEST_STASTICS_RESET,"");
        Collection<Session> sessions = sessionContext.sessions();
        for (Session session:sessions) {
            try {
                rpcCaller.invoke(session,request);
            } catch (Exception e) {
                logger.error("重置统计数据失败:{}",e);
                e.printStackTrace();
            }
        }
    }
}
