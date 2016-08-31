package com.lsxy.framework.rpc.mina.client;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.AbstractSession;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.apache.mina.core.session.IoSession;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/7/30.
 */
public class MinaClientSession extends AbstractSession {
    private IoSession ioSession;

    public MinaClientSession(IoSession ioSession, RPCHandler handler,String serverUrl){
        super(handler);
        this.setId(UUIDGenerator.uuid());
        ioSession.setAttribute("sessionid",this.getId());
        this.ioSession = ioSession;
        this.setServerUrl(serverUrl);
    }

    @Override
    public void concreteWrite(Object object) throws SessionWriteException {
        ioSession.write(object);
    }

    @Override
    public boolean isValid() {
        return this.ioSession != null && this.ioSession.isConnected() && this.ioSession.isActive();
    }

    @Override
    public void close(boolean b) {
        this.ioSession.closeNow();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) ioSession.getRemoteAddress();
    }
}
