package com.lsxy.area.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Createdb by tandy on 16/7/19.
 */
@Component
public class AreaServer {

    private static final Logger logger = LoggerFactory.getLogger(AreaServer.class);


    @PostConstruct
    public void start() throws InterruptedException {

        if (logger.isDebugEnabled()) {
            logger.debug("开始启动区域管理服务器....");
        }
        int port = 8888;
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new HelloServerInitializer());

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();
            // 监听服务器关闭监听
//            f.channel().closeFuture().sync();
            while(true){
                TimeUnit.SECONDS.sleep(1);
                logger.debug("hahahahaha");

            }


            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
//        if (logger.isDebugEnabled()) {
//            logger.debug("区域管理服务器启动成功,绑定端口:{}", port);
//        }

    }
}
