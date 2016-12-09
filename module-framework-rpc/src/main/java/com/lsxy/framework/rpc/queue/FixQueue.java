package com.lsxy.framework.rpc.queue;

import com.lsxy.framework.rpc.api.RPCMessage;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/26.
 * <p>修正队列</p>
 * 消息发送失败,会将消息丢入修正队列<br/>
 * 修正队列最大可存放消息容量为 2147483647  Integer.MAX<br/>
 *
 */
@Component
public class FixQueue implements Runnable{

    //修正队列中的消息过期时间 2分钟
    private static final long FIX_EXPIRED = 2*60*1000;
    @Autowired
    private SessionContext sessionContext;

    private static final Logger logger = LoggerFactory.getLogger(FixQueue.class);

    private LinkedBlockingQueue<RPCMessage> queue = new LinkedBlockingQueue<>();


    /**
     *  将消息放入队列
     *  消息放入队列时会根据队列当前,如果队列满了,会根据FIFO原则丢弃消息
     *  参考queue用法:
     * 1)add(anObject):把anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则招聘异常
     * 2)offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.
     * 3)put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
     * @param message
     */
    public void fix(RPCMessage message) {
        logger.info("[FIX]消息进入修正队列:{},当前已累积消息:{}个"+message,this.queue.size());
        if(message instanceof RPCRequest){
            RPCRequest request = (RPCRequest) message;
            if(request.getName().equals(ServiceConstants.CH_MN_HEARTBEAT_ECHO)){
                return;
            }
        }
        boolean putResult = false;
        //没有放入成功,继续放入
        while(!putResult){
            putResult = queue.offer(message);
            //如果放入不成功,表示队列满了,丢弃消息继续放入,直到放入为止
            if(!putResult){
                RPCMessage dropMessage = queue.poll();
                if(logger.isDebugEnabled()){
                    logger.debug("[FIX]队列满了,丢弃消息:{}",dropMessage);
                }
            }
        }

    }

    /**
     * 启动修正队列监控
     */
    @PostConstruct
    public void start(){
        new Thread(this).start();
    }

    @Override
    public void run() {
        RPCMessage message;
        try {

            if(logger.isDebugEnabled()){
                logger.debug("[FIX]修正队列启动. 监控中....");
            }

            while((message = queue.take())!=null){
                try {

                    long timeoffset = (System.currentTimeMillis() - message.getTimestamp());
                    if(timeoffset >= FIX_EXPIRED){
                        logger.error("[FIX]消息过期（{}）丢失:{}",FIX_EXPIRED,message);
                        continue;
                    }

                    message.tryWriteMark();
                    logger.info("[FIX]尝试重新发送消息:{}",message);
                    Session session = sessionContext.getRightSession(message);
                    if(session != null){
                        session.write(message);
                    }else{
                        logger.error("[FIX]连接丢失了,没有找到有效的会话,10S后尝试重新发送累积的消息,已累积[{}]条消息",this.queue.size());
                        //消息重新放入队列后休息10秒
                        this.fix(message);
                        TimeUnit.SECONDS.sleep(10);
                    }
                } catch (RightSessionNotFoundExcepiton | SessionWriteException e) {
                    this.fix(message);
                    logger.error("[FIX]消息写入失败,重新放入队列:"+message+" 队列积累["+this.queue.size()+"]条消息",e);
                }
            }
        } catch (InterruptedException e) {
            logger.error("修正队列异常",e);
        }
    }
}
