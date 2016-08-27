package com.lsxy.framework.rpc.api.handler;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by liuws on 2016/8/27.
 */
public abstract class RpcRequestHandler implements Handler<RPCRequest,RPCResponse> {

    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    /**
     * 要处理的RPCRequest的name
     * @return
     */
    public abstract String getEventName();

    public abstract void handle(RPCRequest request, Session session) throws IOException;

}
