package com.lsxy.area.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by Tandy on 2016/7/20.
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 以("\n")为结尾分割的 解码器
        pipeline.addLast("framerxxxx", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));

        pipeline.addLast("decoderbbbb", new StringDecoder());
        pipeline.addLast("encodercccc", new StringEncoder());
        // 自己的逻辑Handler
        pipeline.addLast("handlerccc", new HelloServerHandler());
    }
}
