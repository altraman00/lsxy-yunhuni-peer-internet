package com.lsxy.area.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Createdb by tandy on 16/7/19.
 *
 */
@Component
public class   AreaServer {

    private static final Logger logger = LoggerFactory.getLogger(AreaServer.class);


    @PostConstruct
    public void start(){

        if(logger.isDebugEnabled()){
            logger.debug("开始启动区域管理服务器....");
        }
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(),Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new HelloServerHandler());
            }
        });


        int port = 8888;
        bootstrap.bind(new InetSocketAddress(port));
        if(logger.isDebugEnabled()){
            logger.debug("区域管理服务器启动成功,绑定端口:{}",port);
        }

    }

    private static class HelloServerHandler extends SimpleChannelHandler {



        /**

         * 当有客户端绑定到服务端的时候触发，打印"Hello world, I'm server."

         *

         * @alia OneCoder

         * @author lihzh

         */

        @Override

        public void channelConnected(ChannelHandlerContext ctx,

                                     ChannelStateEvent e) {
            if(logger.isDebugEnabled()){
                logger.debug("有客户端连接经来了");
            }
            System.out.println("Hello world, I'm server.");

        }

    }


}
