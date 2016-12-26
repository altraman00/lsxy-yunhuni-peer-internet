package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.rpc.api.client.AbstractClient;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
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
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/1.
 * Netty Client
 */
@Component
@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "netty", matchIfMissing = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(name="com.lsxy.area.agent.AreaClient")
public class NettyClient extends AbstractClient{

    //连接失败次数
    private int errorTimes;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private NettyClientHandler handler;

    private AttributeKey<String> SESSION_ID = AttributeKey.valueOf("sessionid");

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    @Override
    protected Session doBind(String serverUrl) throws ClientBindException {
        Session session = null;
        String host = getHost(serverUrl);
        int port = getPort(serverUrl);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)

            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            //设置outbindbuffer的高低水位，可根据需要调整
            b.option(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(512 * 1024, 1024 * 1024));
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast();
                    ChannelPipeline pipeline = ch.pipeline();
//                    pipeline.addLast("decoder", new RPCMessageDecoder());
//                    pipeline.addLast("encoder", new RPCMessageEncoder());
//                    pipeline.addLast("handler", handler.getIoHandler());
                    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                    pipeline.addLast("decoder", new StringDecoder());
                    pipeline.addLast("encoder", new StringEncoder());

                    ch.pipeline().addLast(handler.getIoHandler());
                }
            });

            logger.info("尝试连接区域服务器[{}]:{}",errorTimes++,serverUrl);

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            //连接成功后,稍微等一下,如果连接时通过nginx做的代理,刚开始连接上,如果后端服务没有READY的情况下,连接成功后,过一会会断开连接
            TimeUnit.SECONDS.sleep(2);

            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.error("客户端连接断开啦。。。。。{}",future.channel());

                    Attribute att =future.channel().attr(AttributeKey.valueOf("sessionid"));
                    if(att != null){
                        String sessionid = (String)att .get();
                        if(logger.isDebugEnabled()){
                            logger.debug("清理会话:{}" , sessionid);
                        }
                        sessionContext.remove(sessionid);
                    }
                    workerGroup.shutdownGracefully();

                }
            });

            if(f.isSuccess() && f.channel().isActive()){
                logger.info("客户端连接成功,准备发送注册客户端命令");
                session = new NettyClientSession(f.channel(),handler,serverUrl);
                f.channel().attr(SESSION_ID).set(session.getId());
               this.doConnect(session);
            }



            // Wait until the connection is closed.
//            f.channel().closeFuture().sync();
        }catch(Exception ex){
            throw new ClientBindException(ex);
        } finally {
//            workerGroup.shutdownGracefully();
        }
        return session;
    }




    private int getPort(String serverUrl) {
        assert serverUrl!=null;
        return Integer.parseInt(serverUrl.substring(serverUrl.indexOf(":") + 1));
    }

    private String getHost(String serverUrl) {
        assert serverUrl!=null;
        return serverUrl.substring(0, serverUrl.indexOf(":"));
    }


}
