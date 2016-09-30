package com.lsxy.framework.rpc.netty.demo.client;

import com.lsxy.framework.rpc.netty.demo.LoopBackTimeStamp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tandy on 16/8/1.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LoopBackTimeStamp ts = (LoopBackTimeStamp) msg;
        ctx.writeAndFlush(ts); //recieved message sent back directly
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.error("异常",cause);
        ctx.close();
    }
}
