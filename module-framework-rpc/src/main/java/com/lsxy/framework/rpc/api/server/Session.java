package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RPCRequest;

/**
 * Created by tandy on 16/7/29.
 */
public interface Session {
    public RPCHandler getRPCHandle();

    void write(Object object);

    String getId();

    boolean isValid();
}
