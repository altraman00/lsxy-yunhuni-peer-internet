package com.lsxy.area.server;


import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Createdb by tandy on 16/7/19.
 */
@Component
public class AreaServer {

    private static final Logger logger = LoggerFactory.getLogger(AreaServer.class);

    @Autowired
    private RemoteServer remoteServer;

    @Value("${area.server.port}")
    private Integer port;



    @Bean
    public AbstractServiceHandler getServiceHandler(){
        return new AbstractServiceHandler() {
            @Override
            public RPCResponse handleService(RPCRequest request, IoSession session) {
                if(logger.isDebugEnabled()){
                    logger.debug("处理响应");
                }
                RPCResponse response = RPCResponse.buildResponse(request);
                response.setMessage(RPCResponse.STATE_OK);
                return response;
            }
        };
    }



    @PostConstruct
    public void start() throws RemoteServerStartException {
        remoteServer.setServerPort(port);
        remoteServer.startServer();
    }




//
//    @PostConstruct
//    public void start() throws InterruptedException {
//
//        if (logger.isDebugEnabled()) {
//            logger.debug("开始启动区域管理服务器....");
//        }
//        int port = 8888;
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup);
//            b.channel(NioServerSocketChannel.class);
//            b.childHandler(new HelloServerInitializer());
//
//            // 服务器绑定端口监听
//            ChannelFuture f = b.bind(port).sync();
//            // 监听服务器关闭监听
////            f.channel().closeFuture().sync();
//            while(true){
//                TimeUnit.SECONDS.sleep(1);
//                logger.debug("hahahahaha");
//
//            }
//
//
//            // 可以简写为
//            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }
////        if (logger.isDebugEnabled()) {
////            logger.debug("区域管理服务器启动成功,绑定端口:{}", port);
////        }
//
//    }
}
