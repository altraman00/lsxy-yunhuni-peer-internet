package com.lsxy.framework.rpc.api.server;

import com.lsxy.framework.rpc.AbstractServiceHandler;
import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class AbstractServerRPCHandler extends RPCHandler {

    @Autowired
    private ServerSessionContext sessionContext;

    private static final Logger logger = LoggerFactory.getLogger(AbstractServerRPCHandler.class);
    
    @Autowired(required = false)
    private AbstractServiceHandler serviceHandler;

    @Autowired
    private RPCCaller rpcCaller;

    public ServerSessionContext getSessionContext() {
        return sessionContext;
    }

    private AtomicLong processRequestCount = new AtomicLong(0);


    /**
     * 查询处理次数
     * @return
     */
    public long getProcessRequestCount(){
        return processRequestCount.longValue();
    }




    /**
     * 消息统一处理入口
     * @param ctxObject  环境对象,根据环境不一样 类型也不一样  iosession in mina   channelcontext in netty
     * @param message
     */
    protected void process(Object ctxObject, RPCMessage message) throws SessionWriteException {
        Session session = getSessionInTheContextObject(ctxObject);
        if(message instanceof RPCRequest){
            RPCRequest request = (RPCRequest) message;
            RPCResponse response = null;
//            if(logger.isDebugEnabled()){
//                logger.debug("消息统一处理入口:{}",message);
//            }


            if(request.getName().equals(ServiceConstants.CH_MN_CONNECT)){
                String areaid = (String) request.getParameter("aid");
                String nodeid = (String) request.getParameter("nid");

                if(serviceHandler.isConnectAvalid(areaid,nodeid)){
                    doConnect(ctxObject,request);
                }else{
                    refuseConnect(ctxObject,request);
                }
            } else if (request.getName().equals(ServiceConstants.CH_MN_HEARTBEAT_ECHO)){    //心跳
                response = RPCResponse.buildResponse(request);
                response.setMessage(RPCResponse.STATE_OK);
            } else {
                if(serviceHandler != null){
                    if(logger.isDebugEnabled()){
                        logger.debug("serviceHandler is :{}" , serviceHandler.getClass().getName());
                    }
                    response = serviceHandler.handleService(request, getSessionInTheContextObject(ctxObject));
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("未找到服务器端ServiceHandler Bean对象,丢弃请求:{}",request);
                    }
                }
            }
            if(response != null && session != null){
                session.write(response);
            }
            processRequestCount.incrementAndGet();

        }else if(message instanceof  RPCResponse){

            RPCResponse response = (RPCResponse) message;
            rpcCaller.receivedResponse(response);
        }else{

        }
    }

    /**
     * 拒绝连接  给客户端响应一个rpcresponse 告诉客户端连接失败
     * @param ctxObject
     * @param request
     */
    protected abstract void refuseConnect(Object ctxObject, RPCRequest request);


    /**
     * 处理客户端连接指令,需要判断是否合法,如果合法,构建对应的Session并丢入sessionContext
     * 在此处理ip白名单
     * @param contextObject
     * @param request
     * @return
     */
    protected abstract Session doConnect(Object contextObject, RPCRequest request) throws SessionWriteException;
}
