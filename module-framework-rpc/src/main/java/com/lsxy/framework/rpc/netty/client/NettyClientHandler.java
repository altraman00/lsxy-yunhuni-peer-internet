package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.client.AbstractClientRPCHandler;
import com.lsxy.framework.rpc.api.client.AbstractClientServiceHandler;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.netty.NettyCondition;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/3.
 */

@Component
@Conditional(NettyCondition.class)
public class NettyClientHandler extends AbstractClientRPCHandler {


    private IOHandler ioHandler = new IOHandler();

    @Autowired(required = false)
    private AbstractClientServiceHandler serviceHandler;


    @Override
    public Session getSessionInTheContextObject(Object ctxObject) {
        return null;
    }


    @ChannelHandler.Sharable
    class IOHandler extends SimpleChannelInboundHandler<RPCMessage>{

        private final Logger logger = LoggerFactory.getLogger(IOHandler.class);
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RPCMessage msg) throws Exception {
            if(logger.isDebugEnabled()){
                logger.debug("收到消息:{}" ,msg);
            }
            process(ctx,msg);
        }
    }

    public IOHandler getIoHandler() {
        return ioHandler;
    }
}
