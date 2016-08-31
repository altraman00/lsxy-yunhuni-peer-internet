package com.lsxy.area.server.event.handler;

import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.SessionContext;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_DIAL_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_DIAL_COMPLETED.class);

    @Autowired(required = false)
    private StasticsCounter sc;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_DIAL_COMPLETED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        logger.info("正在处理{}",getEventName());
        return null;
    }


}
