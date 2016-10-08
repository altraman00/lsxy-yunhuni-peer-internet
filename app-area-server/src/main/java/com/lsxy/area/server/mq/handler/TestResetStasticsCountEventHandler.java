package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.framework.mq.MQStasticCounter;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.apigw.test.TestResetStasticsCountEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Collection;

/**
 * Created by tandy on 16/8/10.
 */
@Component
public class TestResetStasticsCountEventHandler implements MQMessageHandler<TestResetStasticsCountEvent>  {

    private static final Logger logger = LoggerFactory.getLogger(TestResetStasticsCountEventHandler.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    @Autowired(required = false)
    private MQStasticCounter mqsc;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public void handleMessage(TestResetStasticsCountEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("重置统计数据");
        }
        if(sc!=null) sc.reset();
        if(mqsc != null) mqsc.reset();;
        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_TEST_STASTICS_RESET,"");
        Collection<Session> sessions = sessionContext.sessions();
        for (Session session:sessions) {
            try {
                rpcCaller.invoke(sessionContext,request);
            } catch (Exception e) {
                logger.error("重置统计数据失败:{}",e);
            }
        }
    }
}
