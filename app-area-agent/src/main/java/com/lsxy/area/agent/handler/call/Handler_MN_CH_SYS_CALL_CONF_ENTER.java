package com.lsxy.area.agent.handler.call;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcError;
import com.lsxy.app.area.cti.commander.RpcResultListener;
import com.lsxy.area.agent.cti.CTIClientContext;
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
public class Handler_MN_CH_SYS_CALL_CONF_ENTER extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CALL_CONF_ENTER.class);

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private CTIClientContext cticlientContext;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CALL_CONF_ENTER;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse response = RPCResponse.buildResponse(request);

        Client cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }

        Map<String, Object> params = request.getParamMap();
        String res_id = (String)params.get("res_id");
        String call_id = (String)params.get("user_data");

        try {
            cticlient.operateResource(0, 0,res_id, "sys.call.conf_enter", params, new RpcResultListener(){
                @Override
                protected void onResult(Object o) {
                    Map<String,String> params = (Map<String,String>) o;
                    if(logger.isDebugEnabled()){
                        logger.debug("调用sys.call.conf_enter成功call_id={},result={}",call_id,o);
                    }
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method", Constants.EVENT_SYS_CALL_CONF_ENTER_SUCC)
                                    .put("res_id",params.get("res_id"))
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("CTI发送事件%s,失败", Constants.EVENT_SYS_CALL_CONF_ENTER_SUCC);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("调用sys.call.conf_enter失败call_id={},result={}",call_id,rpcError);
                    /*RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_CONF_ENTER_FAIL)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_CONF_ENTER_FAIL);
                    }*/
                }

                @Override
                protected void onTimeout() {
                    logger.error("调用sys.call.conf_enter超时call_id={}",call_id);
                    /*RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_CONF_ENTER_TIMEOUT)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_CONF_ENTER_TIMEOUT);
                    }*/
                }
            });
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            e.printStackTrace();
            response.setMessage(RPCResponse.STATE_EXCEPTION);
        }
        return response;
    }
}
