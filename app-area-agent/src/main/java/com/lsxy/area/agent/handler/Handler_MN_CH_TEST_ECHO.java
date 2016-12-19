package com.lsxy.area.agent.handler;

import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_TEST_ECHO extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_TEST_ECHO.class);

    @Value("${area.agent.client.cti.sip.host}")
    private String ctiHost;

    @Value("${area.agent.client.cti.sip.port}")
    private int ctiPort;

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired(required = false)
    private StasticsCounter sc;


    @Autowired
    private RPCCaller rpcCaller;


    @Autowired
    private SessionContext sessionContext;


    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_TEST_ECHO;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i=0;i<4;i++){
            RPCRequest r = RPCRequest.unserialize("RQ:"+ UUIDGenerator.uuid()+" "+System.currentTimeMillis()+" CH_MN_CTI_EVENT begin_time=1481860550&answer_time=1481860586&from_uri=sip:system@183.63.144.42&to_uri=1000492&cause=0&call_dir=inbound&ipsc_info={process_id%3D11000015349512461}&user_data=");
            try {
                rpcCaller.invoke(session,r);
            } catch (RightSessionNotFoundExcepiton rightSessionNotFoundExcepiton) {
                rightSessionNotFoundExcepiton.printStackTrace();
            } catch (SessionWriteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
