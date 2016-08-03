package com.lsxy.framework.rpc.netty.demo003;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        pipeline.addLast("framerxxxx", new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, Delimiters.lineDelimiter()));

        pipeline.addLast("decoderbbbb", new ByteToMessageDecoder() {
            @Override
            protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
                if(logger.isDebugEnabled()){
                    logger.debug("decode invoked:    "+ ctx.channel());
                }
                if(in.readableBytes() > 0 ){
                    byte[] buffer = new byte[in.readableBytes()];
                    in.readBytes(buffer);
                    if(logger.isDebugEnabled()){
                        logger.debug("read some bytes:" + buffer);
                    }
                    out.add("11111111");
                }
            }
        });
        pipeline.addLast("encodercccc", new StringEncoder(CharsetUtil.UTF_8));
        // 自己的逻辑Handler
        pipeline.addLast("handlerccc", new HelloServerHandler());

    }
}
