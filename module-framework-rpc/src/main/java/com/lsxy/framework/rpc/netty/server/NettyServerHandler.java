package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.server.AbstractServerRPCHandler;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.netty.NettyCondition;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tandy on 16/8/1.
 */
@Component
@Conditional(NettyCondition.class)
public class NettyServerHandler extends AbstractServerRPCHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private SimpleChannelInboundHandler<RPCMessage> ioHandler = new IOHandle();

    private AttributeKey<String> SESSION_ID = AttributeKey.valueOf("sessionid");


    @Override
    public Session getSessionInTheContextObject(Object ctxObject) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) ctxObject;
        String sessionid = ctx.channel().attr(SESSION_ID).get();
        return getSessionContext().getSession(sessionid);
    }


    @ChannelHandler.Sharable
    class IOHandle extends SimpleChannelInboundHandler<RPCMessage>{

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            logger.error("客户端连接断开:{}" ,ctx.channel());

            Session session = getSessionInTheContextObject(ctx);
            if(session != null) {
                if(logger.isDebugEnabled()){
                    logger.debug("清理客户端连接:{}",session.getId());
                }
                getSessionContext().remove(session.getId());
            }
            super.handlerRemoved(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.error("出现异常:{}",cause);
            super.exceptionCaught(ctx, cause);
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RPCMessage message) throws Exception {
            if(logger.isDebugEnabled()){
                logger.debug("收到消息:" + message);
            }
            process(ctx, message);

//
//            if(message instanceof RPCRequest){
//                RPCRequest request = (RPCRequest) message;
//                RPCResponse response = null;
//                if(request.getName().equals(ServiceConstants.CH_MN_CONNECT)){
//                    response = process_CH_MN_CONNECT(iosession,request);
//                }else{
//                    Session session = remoteServer.getSessionContext().getSessionInTheContextObject(iosession.hashCode());
//                    response = serviceHandler.handleService(request,session);
//                }
//
//                logger.debug("<<"+request);
//                if(response!=null){
//                    logger.debug(">>"+response);
//                    iosession.write(response);
//                }
//            }
//            if(message instanceof RPCResponse){
//                RPCResponse response = (RPCResponse) message;
//                logger.debug(">>[NM]"+response);
//                RequestListener rl = requestListeners.get(response.getSessionid());
//                if(rl != null){
//                    rl.recivedResponse(response);
//                    removeRequestListener(rl);
//                }else{
//                    logger.debug("收到响应【"+response.getSessionid()+"】");
//                    remoteServer.getRpcCaller().putResponse(response);
//                }
//
//            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }
    }



    /**
     * 处理连接命令
     * @param contextObject  对于netty  channelcontext   对于mina  iosession
     * @param request
     * @return
     */
    @Override
    protected RPCResponse process_CH_MN_CONNECT(Object contextObject, RPCRequest request) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) contextObject;
        //TODO 需要验证连接的有效性  连接IP白名单验证

        String sessionid = (String) request.getParameter("sessionid");
        Session session = new NettyServerSession(sessionid,ctx.channel(),this);
        getSessionContext().putSession(session);
        ctx.channel().attr(SESSION_ID).setIfAbsent(sessionid);
        RPCResponse response = RPCResponse.buildResponse(request);
        response.setMessage(RPCResponse.STATE_OK);

        logger.info("区域连接成功:{}",ctx.channel());
        return response;
    }

    public SimpleChannelInboundHandler<RPCMessage> getIoHandler() {
        return ioHandler;
    }
}
