package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liuws on 2016/8/27.
 */
public abstract class RpcRequestHandler implements Handler<RPCRequest,RPCResponse> {

    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired(required = false)
    private StasticsCounter sc;

    /**
     * 要处理的RPCRequest的name
     * @return
     */
    public abstract String getEventName();

    public abstract void handle(Client client);

    public RPCResponse handle(RPCRequest request){
        if(logger.isDebugEnabled()){
            logger.debug("收到区域管理器RPCRequest:{}",request);
        }
        /*收到区域管理器请求次数计数*/
        if(sc!=null) sc.getReceivedAreaServerRequestCount().incrementAndGet();

        RPCResponse response = RPCResponse.buildResponse(request);
        try{
            Client client = cticlientContext.getAvalibleClient();
            if(client == null) {
                response.setMessage(RPCResponse.STATE_EXCEPTION);
                return response;
            }
            handle(client);
            response.setMessage(RPCResponse.STATE_OK);
        }catch (Throwable e){
            logger.error("处理RPCRequest出错：",e);
            response.setMessage(RPCResponse.STATE_EXCEPTION);
        }
        if(logger.isDebugEnabled()){
            logger.debug("返回给区域管理器RPCResponse:{}",response);
        }
        return response;
    }
}
