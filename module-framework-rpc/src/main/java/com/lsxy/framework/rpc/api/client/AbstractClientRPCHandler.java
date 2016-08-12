package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class AbstractClientRPCHandler implements RPCHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClientRPCHandler.class);
    
    //注册的监听
    protected Map<String,RequestListener> requestListeners = new HashMap<String,RequestListener>();


    @Autowired(required =false)
    private AbstractClientServiceHandler serviceHandler;

    @Autowired
    private ClientSessionContext sessionContext;


    @Autowired
    private RPCCaller rpcCaller;

    /**
     * 注册监听器
     * @param listener
     */
    public void addRequestListener(RequestListener listener){
        if(requestListeners.get(listener.getSessionId())==null)
            requestListeners.put(listener.getSessionId(),listener);
    }

    /**
     * 移除监听器
     * @param listener
     */
    public void removeRequestListener(RequestListener listener){
        requestListeners.remove(listener.getSessionId());
    }


    /**
     * 消息统一处理入口
     * @param ctxObject  环境对象,根据环境不一样 类型也不一样  iosession in mina   channelcontext in netty
     * @param message
     */
    protected void process(Object ctxObject, RPCMessage message) throws SessionWriteException {
        if(message instanceof RPCRequest){
            RPCRequest request = (RPCRequest) message;
            if(serviceHandler != null){
                 RPCResponse response = serviceHandler.handleService(request, getSessionInTheContextObject(ctxObject));
                logger.debug("<<{}",request);
                if(response != null){
                    if(logger.isDebugEnabled()){
                        logger.debug(">>{}",response);
                    }
                    Session session = getSessionInTheContextObject(ctxObject);
                    session.write(response);
                }
            }else{
                if(logger.isDebugEnabled()){
                    logger.debug("not found service handler bean defined in springcontext,drop request:{}" ,request);
                }
            }
        }else if(message instanceof  RPCResponse){
            RPCResponse response = (RPCResponse) message;
            rpcCaller.receivedResponse(response);
//            if(logger.isDebugEnabled()){
//                logger.debug(">>[NM]"+response);
//            }
//            rpcCaller.putResponse(response);
//            RPCRequest request = rpcCaller.getRequest(response.getSessionid());
//            if(request != null){
//                if(logger.isDebugEnabled()){
//                    logger.debug("通知请求对象该醒了:{}",request);
//                }
//                synchronized (request){
//                    request.notify();
//                }
//            }else{
//                logger.error("收到一个匹配不到请求对象的响应对象:{}",response);
//            }
        }else{
//            ....
        }
    }

    public ClientSessionContext getSessionContext() {
        return sessionContext;
    }
}
