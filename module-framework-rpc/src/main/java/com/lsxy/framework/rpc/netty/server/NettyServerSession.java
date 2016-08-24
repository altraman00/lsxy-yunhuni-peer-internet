package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.api.server.AbstractSession;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.netty.NettySession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/3.
 */
public class NettyServerSession extends NettySession {

    public NettyServerSession(String sessionid,Channel channel, NettyServerHandler nettyServerHandler) {
        super(channel,nettyServerHandler);
        this.setId(sessionid);
    }
}
