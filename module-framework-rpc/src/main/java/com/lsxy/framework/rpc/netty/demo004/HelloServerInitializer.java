package com.lsxy.framework.rpc.netty.demo004;

import com.lsxy.framework.rpc.netty.codec.RPCMessageDecoder;
import com.lsxy.framework.rpc.netty.codec.RPCMessageEncoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tandy on 2016/7/20.
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LoggerFactory.getLogger(HelloServerInitializer.class);
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 以("\n")为结尾分割的 解码器
        pipeline.addLast("decoder",new RPCMessageDecoder());
        pipeline.addLast("encoder",new RPCMessageEncoder());
        // 自己的逻辑Handler
        pipeline.addLast("handlerccc", new HelloServerHandler());

    }
}
