package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;

/**
 * Created by tandy on 16/7/29.
 */
public interface RemoteServer {

    /**
     * 启动服务
     */
    public void startServer() throws RemoteServerStartException;

    void setServerPort(Integer port);
}
