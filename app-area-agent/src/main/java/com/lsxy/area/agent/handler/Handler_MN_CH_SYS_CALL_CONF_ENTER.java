package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcError;
import com.lsxy.app.area.cti.commander.RpcResultListener;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_SYS_CALL_CONF_ENTER extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CALL_CONF_ENTER.class);

    @Value("${area.agent.client.cti.sip.host}")
    private String ctiHost;

    @Value("${area.agent.client.cti.sip.port}")
    private int ctiPort;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ClientSessionContext sessionContext;


    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CALL_CONF_ENTER;
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
            logger.debug("handler process_MN_CH_SYS_CALL_CONF_ENTER:{}",request);
        }

        Map<String, Object> params = request.getParamMap();
        String res_id = (String)params.get("res_id");
        String call_id = (String)request.getParameter("user_data");

        try {
            cticlient.operateResource(0, 0,res_id, "sys.call.conf_enter", params, new RpcResultListener(){
                @Override
                protected void onResult(Object o) {
                    Map<String,String> params = (Map<String,String>) o;
                    if(logger.isDebugEnabled()){
                        logger.debug("sys.call.conf_enter[{}={}]执行成功",res_id,o);
                    }
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method", Constants.EVENT_SYS_CALL_CONF_ENTER_SUCC)
                                    .put("res_id",params.get("res_id"))
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        /*发送区域管理器请求次数计数*/
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败", Constants.EVENT_SYS_CALL_CONF_ENTER_SUCC);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.debug("sys.call.conf_enter[{}={}]执行失败",res_id,rpcError);
                    /*RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_CONF_ENTER_FAIL)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        *//*发送区域管理器请求次数计数*//*
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_CONF_ENTER_FAIL);
                    }*/
                }

                @Override
                protected void onTimeout() {
                    logger.debug("sys.call.conf_enter[{}]执行超时",res_id);
                    /*RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_CONF_ENTER_TIMEOUT)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        *//*发送区域管理器请求次数计数*//*
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_CONF_ENTER_TIMEOUT);
                    }*/
                }
            });
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
        }
        return response;

    }
}
