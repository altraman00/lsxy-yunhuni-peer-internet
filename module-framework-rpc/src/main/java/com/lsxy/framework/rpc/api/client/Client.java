package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.exceptions.ClientBindException;

/**
 * Created by tandy on 16/7/29.
 */
public interface Client {
    /**
     * 客户端连接服务器方法
     * @throws ClientBindException
     */
    void bind() throws ClientBindException;


    /**
     * 设置服务器连接url
     * 支持格式
     * cluster://ip:port|ip:port|ip:port
     * @param serverUrl
     */
    void setServerUrl(String serverUrl);

    /**
     * set area id
     * area id作为客户端标识进行连接
     * @param areaid
     */
    void setClientId(String areaid,String nodeid);
}
