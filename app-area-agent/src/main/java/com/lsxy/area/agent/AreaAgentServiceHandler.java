package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.client.AbstractClientServiceHandler;
import com.lsxy.framework.rpc.api.handler.HandlerManager;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/5.
 * 区域代理业务事件处理器
 */
@Component
public class AreaAgentServiceHandler extends AbstractClientServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(AreaAgentServiceHandler.class);

    @Autowired
    private HandlerManager handlerManager;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Override
    public RPCResponse handleService(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("收到请求:{}",request );
        }

        /*收到区域管理器请求次数计数*/
        if(sc!=null) sc.getReceivedAreaServerRequestCount().incrementAndGet();

        RPCResponse response = handlerManager.fire(request,session);

        if(logger.isDebugEnabled()){
            logger.debug("返回给区域管理器的对象:{}",response);
        }

        return response;
    }

}
