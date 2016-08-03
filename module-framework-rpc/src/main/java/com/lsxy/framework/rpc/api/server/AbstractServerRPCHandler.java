package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class AbstractServerRPCHandler implements RPCHandler {

    @Autowired
    private ServerSessionContext sessionContext;

    private static final Logger logger = LoggerFactory.getLogger(AbstractServerRPCHandler.class);
    
    //注册的监听
    protected Map<String,RequestListener> requestListeners = new HashMap<String,RequestListener>();


    @Autowired
    private AbstractServiceHandler serviceHandler;


    public ServerSessionContext getSessionContext() {
        return sessionContext;
    }

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
    protected void process(Object ctxObject, RPCMessage message) {
        if(message instanceof RPCRequest){
            RPCRequest request = (RPCRequest) message;
            RPCResponse response = null;
            if(request.getName().equals(ServiceConstants.CH_MN_CONNECT)){
                response = process_CH_MN_CONNECT(ctxObject,request);
            }else{
                response = serviceHandler.handleService(request, getSessionInTheContextObject(ctxObject));
            }
            if(response != null){
                Session session = getSessionInTheContextObject(ctxObject);
                session.write(response);
            }
        }else if(message instanceof  RPCResponse){

        }else{

        }
    }




    /**
     * 处理连接命令 客户端连接到服务端,第一件事,发送CH_MN_CONNECT命令到服务器进行注册
     * 如果出现注册失败,就直接结束掉,如果注册成功,则返回成功的响应对象
     * @param ctx  根据环境不同而不同  mina 为 iosession   netty 为channelcontext
     * @param request   解析出来的请求对象
     * @return
     *      如果注册成功并且在IP白名单中,则允许连接  否则拒绝连接
     */
    protected abstract RPCResponse process_CH_MN_CONNECT(Object ctx, RPCRequest request);


}
