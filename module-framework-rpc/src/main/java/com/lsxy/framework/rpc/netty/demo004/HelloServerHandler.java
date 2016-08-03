package com.lsxy.framework.rpc.netty.demo004;

import com.lsxy.framework.rpc.api.RPCMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tandy on 2016/7/20.
 */
public class HelloServerHandler extends SimpleChannelInboundHandler<RPCMessage> {

    private static final Logger logger = LoggerFactory.getLogger(HelloServerHandler.class);



    @Override
    public void channelRead0(ChannelHandlerContext ctx, RPCMessage msg) throws Exception {
        logger.info("SERVER接收到消息:"+msg);

//        ctx.channel().writeAndFlush("yes, server is accepted you ,nice !"+msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) throws Exception {
        logger.warn("Unexpected exception from downstream.", cause);
        ctx.close();
    }
}
