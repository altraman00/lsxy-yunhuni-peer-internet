package com.lsxy.area.server.event;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.Session;

/**
 * Created by liuws on 2016/8/27.
 */
public abstract class EventHandler {

    /**
     * 要处理的RPCRequest的name
     * @return
     */
    public abstract String getEventName();

    public abstract RPCResponse handle(RPCRequest request, Session session);

}
