package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcError;
import com.lsxy.app.area.cti.commander.RpcResultListener;
import com.lsxy.area.agent.StasticsCounter;
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
public class Handler_MN_CH_EXT_DUO_CALLBACK extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_EXT_DUO_CALLBACK.class);



    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_EXT_DUO_CALLBACK;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse response = RPCResponse.buildResponse(request);

        Client cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }

        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_EXT_DUO_CALLBACK:{}",request);
        }

        Map<String, Object> params = request.getParamMap();

        try {
            if(logger.isDebugEnabled()){
                logger.debug("调用CTI创建双向回拔资源，参数为{}", JSONUtil.objectToJson(params));
            }
            cticlient.createResource(0, 0, "ext.duo_callback", params,new RpcResultListener(){

                @Override
                protected void onResult(Object o) {
                    Map<String,String> params = (Map<String,String>) o;
                    if(logger.isDebugEnabled()){
                        logger.debug("调用ext.duo_callback成功，conf_id={},result={}",params.get("user_data"),o);
                    }

                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_EXT_DUO_CALLBACK_SUCCESS)
                                    .put("res_id",params.get("res_id"))
                                    .put("user_data",params.get("user_data"))
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CTI发送事件%s,失败", Constants.EVENT_EXT_DUO_CALLBACK_SUCCESS);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用ext.duo_callback失败call_id={},result={}",params.get("user_data"),rpcError);
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
                    logger.error("调用ext.duo_callback超时call_id={}",params.get("user_data"));
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_ON_TIMEOUT)
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
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
            e.printStackTrace();
        }

        return response;
    }
}
