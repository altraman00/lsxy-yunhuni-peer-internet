package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RPCRequest;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/7/29.
 */
public interface Session {
    RPCHandler getRPCHandle();

    void write(Object object);

    String getId();

    boolean isValid();

    void close(boolean b);

    InetSocketAddress getRemoteAddress();

    String getServerUrl();
}
