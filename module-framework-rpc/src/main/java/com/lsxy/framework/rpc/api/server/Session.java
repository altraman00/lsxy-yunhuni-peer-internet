package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/7/29.
 */
public interface Session {
    RPCHandler getRPCHandle();

    void write(RPCMessage object) throws SessionWriteException;

    String getId();

    boolean isValid();

    void close(boolean b);

    InetSocketAddress getRemoteAddress();

    String getServerUrl();
}
