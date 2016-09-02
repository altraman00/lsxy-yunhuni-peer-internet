package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liups on 2016/8/31.
 */
@Component
public class Handler_EVENT_EXT_DUO_CALLBACK_ON_RELEASED extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_DUO_CALLBACK_ON_RELEASED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_DUO_CALLBACK_ON_RELEASED;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        return null;
    }
}
