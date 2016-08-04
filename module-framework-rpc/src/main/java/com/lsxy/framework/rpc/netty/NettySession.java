package com.lsxy.framework.rpc.netty;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.server.AbstractSession;
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
    public void write(Object object) {

        if(this.isValid() && this.channel.isWritable()){
            if(logger.isDebugEnabled()){
                logger.debug("[{}]>>{}",this.getId(),object);
            }
            channel.writeAndFlush(object);
        }else {
            if(logger.isDebugEnabled()){
                logger.debug("channel is not writable or invalid , drop object {}",object);
            }
        }
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
