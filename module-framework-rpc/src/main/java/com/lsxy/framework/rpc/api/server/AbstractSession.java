package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import com.lsxy.framework.rpc.queue.FixQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class AbstractSession implements Session {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSession.class);

    private RPCHandler handler;

    private String id;

    //作为客户端session时,用于存储连接服务器的url
    private String serverUrl;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    protected AbstractSession(RPCHandler handler) {
        this.handler = handler;
    }


    @Override
    public RPCHandler getRPCHandle() {
        return handler;
    }

    @Override
    public void write(RPCMessage object) throws SessionWriteException {
        if (this.isValid()) {
            if (logger.isDebugEnabled()) {
                logger.debug("[{}]>>{}", this.getId(), object);
            }
            concreteWrite(object);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("channel is not writable or invalid , fix object {}", object);
            }
            throw new SessionWriteException("通道无效,无法写入对象");
        }
    }

    public abstract void concreteWrite(Object object) throws SessionWriteException;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public abstract boolean isValid();


}
