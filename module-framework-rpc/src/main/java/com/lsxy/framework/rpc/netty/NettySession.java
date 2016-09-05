package com.lsxy.framework.rpc.netty;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.AbstractSession;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class NettySession extends AbstractSession {

    private static final Logger logger = LoggerFactory.getLogger(NettySession.class);

    private Channel channel;

    protected NettySession(Channel channel, RPCHandler handler) {
        super(handler);
        this.channel = channel;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public void concreteWrite(Object object) throws SessionWriteException {
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

    @Override
    public boolean isValid() {
        return channel.isActive() && channel.isOpen();
    }
}
