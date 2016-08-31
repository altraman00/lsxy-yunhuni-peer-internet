package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.server.AbstractServerRPCHandler;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import com.lsxy.framework.rpc.netty.NettyCondition;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Created by tandy on 16/8/1.
 */
@Component
@Conditional(NettyCondition.class)
@ConditionalOnBean(NettyRemoteServer.class)
@DependsOn("sessionContext")
public class NettyServerHandler extends AbstractServerRPCHandler {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private SimpleChannelInboundHandler<RPCMessage> ioHandler = new IOHandle();

    private AttributeKey<String> SESSION_ID = AttributeKey.valueOf("sessionid");

    @Autowired
    private ServerSessionContext sessionContext;


    @Override
    public Session getSessionInTheContextObject(Object ctxObject) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) ctxObject;
        String sessionid = ctx.channel().attr(SESSION_ID).get();
        return getSessionContext().getSession(sessionid);
    }


    @ChannelHandler.Sharable
    class IOHandle extends SimpleChannelInboundHandler<RPCMessage>{
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
//                    response = doConnect(iosession,request);
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
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
        }
    }


    /**
     * 拒绝连接消息发给客户端
     * 因为抽象方法中,在未connect的情况下,无法拿到session对象,所以只能在具体实现类中直接响应消息给客户端
     * @param ctxObject
     * @param request
     */
    @Override
    protected void refuseConnect(Object ctxObject, RPCRequest request) {
        RPCResponse response = RPCResponse.buildResponse(request);
        response.setMessage(RPCResponse.STATE_EXCEPTION);
        response.setBody("拒绝连接");
        ChannelHandlerContext ctx = (ChannelHandlerContext) ctxObject;
        ctx.channel().writeAndFlush(response);
    }

    /**
     * 处理连接命令
     * @param contextObject  对于netty  channelcontext   对于mina  iosession
     * @param request
     * @return
     */
    @Override
    protected Session doConnect(Object contextObject, RPCRequest request) throws SessionWriteException {
        ChannelHandlerContext ctx = (ChannelHandlerContext) contextObject;

        String sessionid = (String) request.getParameter("sessionid");
        String areaid = (String) request.getParameter("aid");
        String nodeid = (String) request.getParameter("nid");

        String blankipList = SystemConfig.getProperty("area.server.blank.iplist","");
        String blankipEnabled = SystemConfig.getProperty("area.server.blank.iplist.enabled","false");

        RPCResponse response = RPCResponse.buildResponse(request);
        response.setMessage(RPCResponse.STATE_EXCEPTION);

        InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = isa.getAddress().getHostAddress();

        Session session = null;
        //如果启用了白名单机制,并且非法连接
        if("true".equals(blankipEnabled) && !(blankipList.indexOf(ip)>=0)){
            logger.error("白名单机制启用,连接ip["+ip+"]不在白名单中["+blankipList+"],拒绝连接");
            response.setBody("非法连接,被拒绝");
            ctx.channel().writeAndFlush(response);
            return null;
        }

        //判断是否重复连接
        if(sessionContext.getSessionByArea(areaid,nodeid) != null){
            logger.error("节点尝试重复连接["+areaid+"]["+nodeid+"],被拒绝!");
            response.setBody("节点尝试重复连接["+areaid+"]["+nodeid+"],被拒绝!");
            ctx.channel().writeAndFlush(response);
            return null;
        }
        //所有条件满足,连接成功,将session放入sessioncontext
        session = new NettyServerSession(sessionid,ctx.channel(),this);
        getSessionContext().putSession(areaid,nodeid,session);
        ctx.channel().attr(SESSION_ID).setIfAbsent(sessionid);
        response.setMessage(RPCResponse.STATE_OK);
        logger.info("区域连接成功:{}",ctx.channel());
        session.write(response);
        return session;
    }

    public SimpleChannelInboundHandler<RPCMessage> getIoHandler() {
        return ioHandler;
    }
}
