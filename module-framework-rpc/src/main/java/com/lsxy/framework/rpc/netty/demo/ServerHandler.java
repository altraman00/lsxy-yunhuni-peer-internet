package com.lsxy.framework.rpc.netty.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tandy on 16/8/1.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ChannelInboundHandlerAdapter.class);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LoopBackTimeStamp ts = (LoopBackTimeStamp) msg;
        ts.setRecvTimeStamp(System.nanoTime());

        System.out.println("loop delay in ms : " + 1.0 * ts.timeLapseInNanoSecond() / 1000000L);
    }

    // Here is how we send out heart beat for idle to long
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.ALL_IDLE) { // idle for no read and write
                ctx.writeAndFlush(new LoopBackTimeStamp());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        logger.error("异常",cause);
        ctx.close();
    }
}