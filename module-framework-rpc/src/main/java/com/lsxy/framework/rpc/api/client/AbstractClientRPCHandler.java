package com.lsxy.framework.rpc.api.client;

import com.lsxy.framework.rpc.api.*;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by tandy on 16/8/3.
 */
public abstract class AbstractClientRPCHandler extends RPCHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractClientRPCHandler.class);



    @Autowired(required =false)
    private AbstractClientServiceHandler serviceHandler;

    @Autowired
    private ClientSessionContext sessionContext;


    @Autowired
    private RPCCaller rpcCaller;




    /**
     * 消息统一处理入口
     * @param ctxObject  环境对象,根据环境不一样 类型也不一样  iosession in mina   channelcontext in netty
     * @param message
     */
    protected void process(Object ctxObject, RPCMessage message) throws SessionWriteException {
        Session session = getSessionInTheContextObject(ctxObject);

        if(message instanceof RPCRequest){
            RPCRequest request = (RPCRequest) message;
            if(serviceHandler != null){
                 RPCResponse response = serviceHandler.handleService(request, getSessionInTheContextObject(ctxObject));
                logger.debug("<<{}",request);
                if(response != null){
                    if(logger.isDebugEnabled()){
                        logger.debug(">>{}",response);
                    }

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
        }else{
//            ....
        }
    }

    public ClientSessionContext getSessionContext() {
        return sessionContext;
    }
}
