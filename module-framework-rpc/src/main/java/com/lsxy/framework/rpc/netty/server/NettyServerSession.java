package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.api.server.ServerSession;
import com.lsxy.framework.rpc.netty.NettySession;
import io.netty.channel.Channel;

/**
 * Created by tandy on 16/8/3.
 */
public class NettyServerSession extends NettySession implements ServerSession {

    private long clientStartTimestamp;
    private String clientVersion;

    public NettyServerSession(String sessionid, Channel channel, NettyServerHandler nettyServerHandler,long clientStartTimestamp, String clientVersion) {
        super(channel,nettyServerHandler);
        this.clientStartTimestamp = clientStartTimestamp;
        this.clientVersion = clientVersion;
        this.setId(sessionid);
    }

    @Override
    public long getClientStartTimestamp() {
        return clientStartTimestamp;
    }

    @Override
    public String getClientVersion() {
        return clientVersion;
    }
}
