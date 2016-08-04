package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class AbstractSession implements  Session {

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

    protected AbstractSession(RPCHandler handler){
        this.handler = handler;
    }


    @Override
    public RPCHandler getRPCHandle() {
        return handler;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public abstract  boolean isValid();

}
