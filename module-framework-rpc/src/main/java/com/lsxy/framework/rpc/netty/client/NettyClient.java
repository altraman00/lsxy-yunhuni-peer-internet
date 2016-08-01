package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.rpc.api.client.AbstractClient;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tandy on 16/8/1.
 * Netty Client
 */
public class NettyClient extends AbstractClient{

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    @Override
    protected Session doBind(String serverUrl) {
        return null;
    }

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 8888;
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast();
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    pipeline.addLast("decoder", new StringDecoder());
                    pipeline.addLast("encoder", new StringEncoder());
//                    ch.writeAndFlush("连接上了,你看到了蛮");
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)
            if(logger.isDebugEnabled()){
                logger.debug("发消息了");
            }
            f.channel().writeAndFlush("发消息啦");



            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
