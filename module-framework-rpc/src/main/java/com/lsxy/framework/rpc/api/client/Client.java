package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;

/**
 * Created by tandy on 16/7/29.
 */
public interface Client {
    void bind() throws ClientBindException;

    void setServerUrl(String serverUrl);

    void setClientId(String clientId);

    /**
     * 拿到一个有效的session并返回
     * @return
     */
    Session getAvalibleSession();
}
