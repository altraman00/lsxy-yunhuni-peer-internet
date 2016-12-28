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
 * Created by liups on 2016/9/8.
 */
@Component
public class Handler_MN_CH_EXT_DUO_CALLBACK_CANCEL extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_EXT_DUO_CALLBACK_CANCEL.class);

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_EXT_DUO_CALLBACK_CANCEL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        Commander cticlient = cticlientContext.getAvalibleClient();

        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_EXT_DUO_CALLBACK_CANCEL:{}",request);
        }

        Map<String, Object> params = request.getParamMap();
        String call_id = (String)params.get("user_data");
        try {
            if(logger.isDebugEnabled()){
                logger.debug("调用CTI取消双向回拔，参数为{}", JSONUtil.objectToJson(params));
            }
            cticlient.operateResource(new BusAddress((byte)0,(byte)0), (String) params.get("res_id"),"ext.duo_callback.cancel", params,new RpcResultListener(){

                @Override
                protected void onResult(Object o) {
                    if(logger.isDebugEnabled()){
                        logger.debug("调用ext.duo_callback.cancel成功，call_id={},result={}",call_id,o);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用ext.duo_callback失败call_id={},result={}",call_id,rpcError);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_EXT_CALL_ON_FAIL)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req,true);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_EXT_CALL_ON_FAIL,e);
                    }
                }

                @Override
                protected void onTimeout() {
                    logger.error("调用ext.duo_callback超时call_id={}",call_id);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_EXT_CALL_ON_TIMEOUT)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req,true);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_EXT_CALL_ON_TIMEOUT,e);
                    }
                }
            });
        } catch (Throwable e) {
            logger.error("操作CTI资源异常{}",request,e);
        }

        return null;
    }
}
