package com.lsxy.framework.rpc.mina.server;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.server.Session;
import org.apache.mina.core.session.IoSession;

/**
 * Created by tandy on 16/7/30.
 */
public class MinaServerSession implements Session {
    private IoSession ioSession;

    private RPCHandler handler;

    private String id;

    public MinaServerSession(IoSession ioSession, RPCHandler handler){
        this.ioSession = ioSession;
        this.handler = handler;
        this.id = (String) ioSession.getAttribute("clientId");
    }
//
//    public MinaServerSession() {
//
//    }
//
//    /**
//     * 根据iosession构建session对象
//     * @param ioSession
//     * @param handler
//     * @return
//     */
//    public static MinaServerSession buildByIOSession(IoSession ioSession, RPCHandler handler){
//        MinaServerSession minaSession = new MinaServerSession();
//        minaSession.id = (String) ioSession.getAttribute("clientId");
//        minaSession.handler = handler;
//        minaSession.ioSession = ioSession;
//        return minaSession;
//    }


    @Override
    public RPCHandler getRPCHandle() {
        return handler;
    }

    @Override
    public void write(Object object) {
        ioSession.write(object);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public boolean isValid() {
        return this.ioSession != null && this.ioSession.isConnected() && this.ioSession.isActive();
    }
}
