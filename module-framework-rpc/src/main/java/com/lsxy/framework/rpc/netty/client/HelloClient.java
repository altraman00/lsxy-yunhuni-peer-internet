package com.lsxy.framework.rpc.netty.client;

import ch.qos.logback.core.util.TimeUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/1.
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new HelloClientHandler());

            // 连接服务端
            Channel ch = b.connect("localhost", 8888).sync().channel();


            while(true){
                String value = System.currentTimeMillis()+"";
                System.out.println("写入:" + value);
                ch.writeAndFlush(value + "\r\n");
                TimeUnit.SECONDS.sleep(2);
            }

        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}
