package com.lsxy.area.server.handler;

import com.lsxy.area.server.event.EventManager;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_CH_MN_CTI_EVENT extends RpcRequestHandler{

    @Autowired
    private EventManager eventManager;

    @Override
    public String getEventName() {
        return ServiceConstants.CH_MN_CTI_EVENT;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(true) return null;
        return eventManager.fire(request,session);
    }


}
