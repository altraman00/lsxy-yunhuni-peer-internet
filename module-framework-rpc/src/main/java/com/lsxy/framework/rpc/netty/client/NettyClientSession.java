package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.server.AbstractSession;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/3.
 */
public class NettyClientSession extends AbstractSession {

    private Channel channel;

    private String serverUrl;


    protected NettyClientSession(Channel channel,RPCHandler handler,String serverUrl) {
        super(handler);
        this.setId(UUIDGenerator.uuid());
        this.channel = channel;
        this.serverUrl = serverUrl;
    }

    @Override
    public void write(Object object) {
        channel.writeAndFlush(object);
    }

    @Override
    public void close(boolean b) {
        channel.close();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    public String getServerUrl() {
        return serverUrl;
    }
}
