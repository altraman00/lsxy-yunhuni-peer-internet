package com.lsxy.area.agent.handler;

import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
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

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_TEST_ECHO;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse response = RPCResponse.buildResponse(request);
        response.setMessage("OK");
        return response;
    }
}
