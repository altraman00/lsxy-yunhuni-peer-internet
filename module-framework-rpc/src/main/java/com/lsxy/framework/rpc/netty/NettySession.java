package com.lsxy.framework.rpc.netty;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.AbstractSession;
import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOutboundBuffer;
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

        if(logger.isDebugEnabled()){
            ChannelOutboundBuffer ob = channel.unsafe().outboundBuffer();
            int nioBufferCount = ob.nioBufferCount();
            long nioBufferSize = ob.nioBufferSize();
            int bufferSize = ob.size();
            long totalPendingWriteBytes = ob.totalPendingWriteBytes();
            if(logger.isDebugEnabled()){
                logger.debug("write msg ["+ ((RPCMessage)object).getSessionid()+"] outbound buffer params nioBufferCount={},nioBufferSize={},bufferSize={},totalPendingWriteBytes={}",nioBufferCount,nioBufferSize,bufferSize,totalPendingWriteBytes);
            }
        }
        //在此做限流控制，默认 ChannelOutboundBuffer  默认高水位是64K，低水位是32K
        if(channel.isWritable()) {
            channel.writeAndFlush(object);
        }else{
            throw new SessionWriteException("并发量过大，消息被丢弃："+object);
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
