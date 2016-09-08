package com.lsxy.framework.rpc.api.handler;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.session.Session;

/**
 * Created by liuws on 2016/8/27.
 */
public abstract class RpcRequestHandler{

    /**
     * 要处理的RPCRequest的name
     * @return
     */
    public abstract String getEventName();

    public abstract RPCResponse handle(RPCRequest request, Session session);


}
