package com.lsxy.framework.rpc.mina;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.server.Session;
import org.apache.mina.core.session.IoSession;

import java.util.UUID;

/**
 * Created by tandy on 16/7/30.
 */
public class MinaSession implements Session {
    private IoSession ioSession;

    private RPCHandler handler;

    private String id;

    public MinaSession(IoSession ioSession,RPCHandler handler){
        this.ioSession = ioSession;
        this.handler = handler;
        this.id = UUIDGenerator.uuid();
    }


    @Override
    public RPCHandler getRPCHandle() {
        return handler;
    }

    @Override
    public void write(RPCRequest request) {
        ioSession.write(request);
    }

    @Override
    public Long getId() {
        return ioSession.getId();
    }

    @Override
    public boolean isValid() {
        return this.ioSession != null && this.ioSession.isConnected() && this.ioSession.isActive();
    }
}
