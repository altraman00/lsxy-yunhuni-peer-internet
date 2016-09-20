package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.client.AbstractClientRPCHandler;
import com.lsxy.framework.rpc.api.session.Session;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/3.
 */

@Component
@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "netty", matchIfMissing = false)
@ConditionalOnBean(NettyClient.class)
public class NettyClientHandler extends AbstractClientRPCHandler {

    private IOHandler ioHandler = new IOHandler();

    @Override
    public Session getSessionInTheContextObject(Object ctxObject) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) ctxObject;
        String sessionid = (String) ctx.channel().attr(AttributeKey.valueOf("sessionid")).get();
        assert sessionid != null;
        Session session = getSessionContext().getSession(sessionid);
        assert session != null;
        return session;
    }

    @ChannelHandler.Sharable
    class IOHandler extends SimpleChannelInboundHandler<RPCMessage>{

        private final Logger logger = LoggerFactory.getLogger(IOHandler.class);
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RPCMessage msg) throws Exception {
            if(logger.isDebugEnabled()){
                logger.debug("收到消息:{}" ,msg);
            }
            process(ctx, msg);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            Session session = getSessionInTheContextObject(ctx);
            if(session != null){
                logger.error("服务器连接断开:[{}]-{}",session.getId(),session.getRemoteAddress());
                getSessionContext().remove(session.getId());
            }
            super.channelInactive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("出现了异常:{}",cause);
        }
    }

    public IOHandler getIoHandler() {
        return ioHandler;
    }
}
