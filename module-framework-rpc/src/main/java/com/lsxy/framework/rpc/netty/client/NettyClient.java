package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.rpc.api.RPCHandler;
import com.lsxy.framework.rpc.api.client.AbstractClient;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.ClientBindException;
import com.lsxy.framework.rpc.netty.NettyCondition;
import com.lsxy.framework.rpc.netty.codec.RPCMessageDecoder;
import com.lsxy.framework.rpc.netty.codec.RPCMessageEncoder;
import com.lsxy.framework.rpc.netty.demo004.HelloClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/1.
 * Netty Client
 */
@Component
@Conditional(NettyCondition.class)
public class NettyClient extends AbstractClient{

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
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast();
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast("decoder", new RPCMessageDecoder());
                    pipeline.addLast("encoder", new RPCMessageEncoder());
                    pipeline.addLast("handler", handler.getIoHandler());

//                    ch.writeAndFlush("连接上了,你看到了蛮");
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            session = new NettyClientSession(f.channel(),handler,serverUrl);

            f.channel().attr(SESSION_ID).set(session.getId());

            this.doConnect(session);

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
