package com.lsxy.area.agent.handler.call;

import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_SYS_CALL extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CALL.class);

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CALL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse response = RPCResponse.buildResponse(request);

//        Commander cticlient = cticlientContext.getAvalibleClient();
//        if(cticlient == null) {
//            response.setMessage(RPCResponse.STATE_EXCEPTION);
//            return response;
//        }
//        try {
//            Map<String, Object> params = request.getParamMap();
//            String call_id = (String)params.get("user_data");
//
//            cticlient.createResource(new BusAddress((byte)0,(byte)0), "sys.call", params, new RpcResultListener(){
//                @Override
//                protected void onResult(Object o) {
//                    Map<String,String> params = (Map<String,String>) o;
//                    if(logger.isDebugEnabled()){
//                        logger.debug("调用sys.call成功call_id={},result={}",call_id,o);
//                    }
//
//                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
//                            new MapBuilder<String,Object>()
//                                    .put("method", Constants.EVENT_SYS_CALL_ON_START)
//                                    .put("res_id",params.get("res_id"))
//                                    .put("user_data",call_id)
//                                    .build());
//                    try {
//                        rpcCaller.invoke(sessionContext,req);
//                    } catch (Exception e) {
//                        logger.error("CTI发送事件%s,失败", Constants.EVENT_SYS_CALL_ON_START,e);
//                    }
//                }
//
//                @Override
//                protected void onError(RpcError rpcError) {
//                    logger.error("调用sys.call失败call_id={},result={}",call_id,rpcError);
//                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
//                            new MapBuilder<String,Object>()
//                                    .put("method",Constants.EVENT_SYS_CALL_ON_FAIL)
//                                    .put("user_data",call_id)
//                                    .build());
//                    try {
//                        rpcCaller.invoke(sessionContext,req);
//                    } catch (Exception e) {
//                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_ON_FAIL,e);
//                    }
//                }
//
//                @Override
//                protected void onTimeout() {
//                    logger.error("调用sys.call超时call_id={}",call_id);
//                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
//                            new MapBuilder<String,Object>()
//                                    .put("method",Constants.EVENT_SYS_CALL_ON_TIMEOUT)
//                                    .put("user_data",call_id)
//                                    .build());
//                    try {
//                        rpcCaller.invoke(sessionContext,req);
//                    } catch (Exception e) {
//                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_ON_TIMEOUT,e);
//                    }
//                }
//            });
            response.setMessage(RPCResponse.STATE_OK);
//        } catch (IOException e) {
//            logger.error("创建资源失败",e);
//            response.setMessage(RPCResponse.STATE_EXCEPTION);
//        }
        return response;
    }
}
