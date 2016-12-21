package com.lsxy.framework.rpc.netty.client;

import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.client.AbstractClientRPCHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


/**
 * Created by tandy on 16/8/3.
 */

@Component
@ConditionalOnProperty(value = "global.rpc.provider", havingValue = "netty", matchIfMissing = false)
@ConditionalOnBean(NettyClient.class)
public class NettyClientHandler extends AbstractClientRPCHandler {


    private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
    // 业务逻辑线程池(业务逻辑最好跟netty io线程分开处理，线程切换虽会带来一定的性能损耗，但可以防止业务逻辑阻塞io线程)
    private final static ExecutorService workerThreadService = newBlockingExecutorsUseCallerRun(20,500);

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
    class IOHandler extends SimpleChannelInboundHandler<String>{

        private final Logger logger = LoggerFactory.getLogger(IOHandler.class);
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//            if(logger.isDebugEnabled()){
//                logger.debug("收到消息:{}",msg);
//            }
            RPCMessage rpcMessage = RPCMessage.unserialize(msg);
            if(logger.isDebugEnabled()){
                logger.debug("收到消息耗时:{} ms  [{}]",(System.currentTimeMillis() - rpcMessage.getTimestamp()),rpcMessage.getSessionid());
            }

            workerThreadService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        process(ctx, rpcMessage);
                    } catch (SessionWriteException e) {
                        logger.error("处理RPC消息异常:"+rpcMessage,e);
                    }
                }
            });
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
            cause.printStackTrace();
            logger.error("出现了异常:",cause);
        }
    }

    /**
     * 阻塞的ExecutorService
     *
     * @param maxsize
     * @return
     */
    public static ExecutorService newBlockingExecutorsUseCallerRun(int minsize,int maxsize) {
        return new ThreadPoolExecutor(minsize, maxsize, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

                        try {
                            logger.error("线程不够用了，任务处理被拒绝，重新加入");
                            executor.getQueue().put(r);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
    public IOHandler getIoHandler() {
        return ioHandler;
    }
}
