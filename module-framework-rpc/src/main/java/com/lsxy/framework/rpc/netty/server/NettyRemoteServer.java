package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.exceptions.RemoteServerStartException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/8/1.
 * Netty Remote Server
 */
@Component
public class NettyRemoteServer implements RemoteServer {

    public static final Logger logger = LoggerFactory.getLogger(NettyRemoteServer.class);
    private Integer port;

    @Autowired
    private NettyServerHandler handler;

//    @Autowired
//    private ChannelHandler handler;

    @Override
    public void startServer() throws RemoteServerStartException {
        if (logger.isDebugEnabled()) {
            logger.debug("开始启动区域管理服务器....");
        }
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            b.childHandler(new ChannelInitializer() {
                @Override
                public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                    if(logger.isDebugEnabled()){
                        logger.debug("连接断开了:"+ctx.channel());
                    }
                    super.channelInactive(ctx);
                }


                @Override
                public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                    if(logger.isDebugEnabled()){
                        logger.debug("连接断开啦x"+ctx.channel());
                    }
                    super.channelUnregistered(ctx);
                }

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.closeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            if(logger.isDebugEnabled()){
                                logger.debug("断开啦!!!!!!!!!!!!");
                            }
                        }
                    });
//                    channel.pipeline().addLast("frameDecoder", new DelimiterBasedFrameDecoder(1024,Delimiters.lineDelimiter()));
//                    channel.pipeline().addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));

                    ChannelPipeline pipeline = channel.pipeline();
                    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    pipeline.addLast("decoder", new StringDecoder());
                    pipeline.addLast("encoder", new StringEncoder());

                    channel.pipeline().addLast(handler);


                    if(logger.isDebugEnabled()){
                        logger.debug("有新的接入channel:" + channel);
                    }


                }
            });

            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();

            if (logger.isDebugEnabled()) {
                logger.debug("RCP服务器启动成功,绑定端口:{}", port);
            }

            // 阻塞住,知道被通知结束
            f.channel().closeFuture().sync();

            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RemoteServerStartException(ex);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void setServerPort(Integer port) {
        this.port = port;
    }

    public void start() throws InterruptedException {



    }
}
