package com.lsxy.area.agent.handler.simplecall;

import com.lsxy.app.area.cti.BusAddress;
import com.lsxy.app.area.cti.Commander;
import com.lsxy.app.area.cti.RpcError;
import com.lsxy.app.area.cti.RpcResultListener;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_EXT_NOTIFY_CALL extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_EXT_NOTIFY_CALL.class);

    @Autowired
    private CTIClientContext cticlientContext;


    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_EXT_NOTIFY_CALL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse response = RPCResponse.buildResponse(request);

        Commander cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }

        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_EXT_NOTIFY_CALL:{}",request);
        }

        Map<String, Object> params = request.getParamMap();
        try {
            if(logger.isDebugEnabled()){
                logger.debug("调用CTI创建语音外呼资源，参数为{}", JSONUtil.objectToJson(params));
            }
            String res_id = cticlient.createResource(new BusAddress((byte)0,(byte)0), "ext.notify_call", params, new RpcResultListener(){

                @Override
                protected void onResult(Object o) {
                    Map<String,String> params = (Map<String,String>) o;
                    if(logger.isDebugEnabled()){
                        logger.debug("调用ext.notify_call成功，callId={},result={}",params.get("user_data"),o);
                    }

                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method", Constants.EVENT_EXT_NOTIFY_CALL_SUCCESS)
                                    .put("res_id",params.get("res_id"))
                                    .put("user_data",params.get("user_data"))
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CTI发送事件%s,失败", Constants.EVENT_EXT_NOTIFY_CALL_SUCCESS);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用ext.notify_call失败call_id={},result={}",params.get("user_data"),rpcError);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_EXT_CALL_ON_FAIL)
                                    .put("user_data",params.get("user_data"))
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_EXT_CALL_ON_FAIL);
                    }
                }

                @Override
                protected void onTimeout() {
                    logger.error("调用ext.notify_call超时call_id={}",params.get("user_data"));
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_EXT_CALL_ON_TIMEOUT)
                                    .put("user_data",params.get("user_data"))
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_EXT_CALL_ON_TIMEOUT);
                    }
                }
            });
            response.setMessage(RPCResponse.STATE_OK);
            response.setBody(res_id);
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
            e.printStackTrace();
        }

        return response;
    }
}