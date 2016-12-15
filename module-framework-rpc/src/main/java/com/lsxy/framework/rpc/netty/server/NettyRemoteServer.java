package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.api.server.AbstractServerRPCHandler;
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
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/8/1.
 * Netty Remote Server
 */
@Component
@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "netty", matchIfMissing = false)
@ConditionalOnClass(name="com.lsxy.area.server.AreaServer")
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
                protected void initChannel(Channel channel) throws Exception {
                    channel.closeFuture().addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture channelFuture) throws Exception {
                            String sessionid = (String) channelFuture.channel().attr(AttributeKey.valueOf("sessionid")).get();
                            assert sessionid!=null;
                            logger.info("客户端连接断开,清理会话[{}-{}]",sessionid,channelFuture.channel());
                            handler.getSessionContext().remove(sessionid);
                        }
                    });

                    ChannelPipeline pipeline = channel.pipeline();
////
////                    pipeline.addLast("decoder",new RPCMessageDecoder());
////                    pipeline.addLast("encoder",new RPCMessageEncoder());
                    channel.pipeline().addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    // 字符串解码 和 编码
                     pipeline.addLast("decoder", new StringDecoder());
                     pipeline.addLast("encoder", new StringEncoder());

                    channel.pipeline().addLast(handler.getIoHandler());

                    logger.info("客户端尝试连接区域管理器:{}:" , channel);
                }
            });

            // 服务器绑定端口监听
            b.bind(port).sync();

            if (logger.isDebugEnabled()) {
                logger.debug("RCP服务器启动成功,绑定端口:{}", port);
            }

            // 阻塞住,知道被通知结束
//            f.channel().closeFuture().sync();

            // 可以简写为
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        }catch(Exception ex){
            throw new RemoteServerStartException(ex);
        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }

    }

    @Override
    public void setServerPort(Integer port) {
        this.port = port;
    }

    @Override
    public AbstractServerRPCHandler getHandler() {
        return this.handler;
    }

}
