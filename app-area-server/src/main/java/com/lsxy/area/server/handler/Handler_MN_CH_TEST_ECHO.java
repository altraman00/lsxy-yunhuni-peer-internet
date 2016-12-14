package com.lsxy.area.server.handler;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import org.springframework.stereotype.Component;

import static com.lsxy.utils.MyInterceptorClassName.logger;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_MN_CH_TEST_ECHO extends RpcRequestHandler{

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_TEST_ECHO;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("处理:MN_CH_TEST_ECHO");
        }

        RPCResponse response = RPCResponse.buildResponse(request);
        response.setTimestamp(request.getTimestamp());
        response.setMessage(RPCResponse.STATE_OK);
        return response;
    }
}
