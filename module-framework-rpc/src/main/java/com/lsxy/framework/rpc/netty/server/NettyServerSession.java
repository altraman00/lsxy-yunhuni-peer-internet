package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.netty.NettySession;
import io.netty.channel.Channel;

/**
 * Created by tandy on 16/8/3.
 */
public class NettyServerSession extends NettySession {

    public NettyServerSession(String sessionid,Channel channel, NettyServerHandler nettyServerHandler) {
        super(channel,nettyServerHandler);
        this.setId(sessionid);
    }
}
