package com.lsxy.framework.rpc.netty.server;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.server.AbstractServerRPCHandler;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.*;

/**
 * Created by tandy on 16/8/1.
 */
@Component
@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "netty", matchIfMissing = false)
@ConditionalOnBean(NettyRemoteServer.class)
@DependsOn("sessionContext")
public class NettyServerHandler extends AbstractServerRPCHandler {

    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
//    private final static ExecutorService workerThreadService = newBlockingExecutorsUseCallerRun(500);


    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private SimpleChannelInboundHandler<String> ioHandler = new IOHandle();

    private AttributeKey<String> SESSION_ID = AttributeKey.valueOf("sessionid");

    @Autowired
    private ServerSessionContext sessionContext;


    /**
     * 阻塞的ExecutorService
     *
     * @param size
     * @return
     */
    public static ExecutorService newBlockingExecutorsUseCallerRun(int size) {
        return new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        try {
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
    @Override
    public Session getSessionInTheContextObject(Object ctxObject) {
        ChannelHandlerContext ctx = (ChannelHandlerContext) ctxObject;
        String sessionid = ctx.channel().attr(SESSION_ID).get();
        return getSessionContext().getSession(sessionid);
    }


    @ChannelHandler.Sharable
    class IOHandle extends SimpleChannelInboundHandler<String>{
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//            if(logger.isDebugEnabled()){
//                logger.debug("收到消息：{}",msg);
//            }
            final RPCMessage rpcMessage = RPCMessage.unserialize(msg);
            if(logger.isDebugEnabled()){
                logger.debug("收到消息["+msg+"]耗时:"+(System.currentTimeMillis() - rpcMessage.getTimestamp())+"ms");
            }
//            workerThreadService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
                        process(ctx, rpcMessage);
//                    } catch (SessionWriteException e) {
//                        logger.error("处理RPC消息异常:"+rpcMessage,e);
//                    }
//                }
//            });
        }
        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            logger.error("客户端连接断开:"+ctx.channel() );

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
            logger.error("出现异常:"+cause.getMessage(),cause);
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

    public SimpleChannelInboundHandler<String> getIoHandler() {
        return ioHandler;
    }
}
