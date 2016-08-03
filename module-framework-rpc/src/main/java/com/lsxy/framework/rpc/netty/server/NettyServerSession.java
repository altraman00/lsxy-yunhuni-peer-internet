package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.api.server.AbstractSession;
import com.lsxy.framework.rpc.api.server.Session;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/3.
 */
public class NettyServerSession extends AbstractSession {

    private Channel channel;

    public NettyServerSession(String sessionid,Channel channel, NettyServerHandler nettyServerHandler) {
        super(nettyServerHandler);
        this.channel = channel;
        this.setId(sessionid);
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
}
