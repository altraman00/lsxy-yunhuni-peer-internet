package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.server.AbstractSession;
import com.lsxy.framework.rpc.netty.NettySession;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/3.
 */
public class NettyClientSession extends NettySession {

    private String serverUrl;


    protected NettyClientSession(Channel channel,RPCHandler handler,String serverUrl) {
        super(channel,handler);
        this.setId(UUIDGenerator.uuid());
        this.serverUrl = serverUrl;
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
