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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/8/27.
 */
@Component
public class Handler_MN_CH_SYS_CALL extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CALL.class);

    @Value("${area.agent.client.cti.sip.host}")
    private String ctiHost;

    @Value("${area.agent.client.cti.sip.port}")
    private int ctiPort;

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ClientSessionContext sessionContext;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_SYS_CALL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_SYS_CALL:{}",request);
        }
        RPCResponse response = RPCResponse.buildResponse(request);

        String to = (String) request.getParameter("to");
        Integer maxAnswerSec = (Integer) request.getParameter("maxAnswerSec");
        Integer maxRingSec = (Integer) request.getParameter("maxRingSec");

        assert  to != null;
        assert maxAnswerSec != null;
        assert maxRingSec!=null;

        Client cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }
        try {
            Map<String, Object> params = new HashMap<>();
            String call_id = (String)request.getParameter("user_data");
            params.put("from_uri", request.getParameter("from"));
            params.put("to_uri", request.getParameter("to") + "@" + ctiHost + ":" + ctiPort);
            params.put("max_answer_seconds", maxAnswerSec);
            params.put("max_ring_seconds", maxRingSec);
            params.put("user_data",call_id);

            cticlient.createResource(0, 0, "sys.call", params, new RpcResultListener(){
                @Override
                protected void onResult(Object o) {
                    Map<String,String> params = (Map<String,String>) o;
                    if(logger.isDebugEnabled()){
                        logger.debug("资源{}[{}={}]创建成功",getEventName(),call_id,o);
                    }

                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method", Constants.EVENT_SYS_CALL_ON_START)
                                    .put("res_id",params.get("res_id"))
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        /*发送区域管理器请求次数计数*/
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败", Constants.EVENT_SYS_CALL_ON_START);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("资源{}[{}]创建失败:{}",getEventName(),call_id,rpcError);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_ON_FAIL)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        /*发送区域管理器请求次数计数*/
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_ON_FAIL);
                    }
                }

                @Override
                protected void onTimeout() {
                    logger.error("资源{}[{}]创建超时",getEventName(),call_id);
                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.CH_MN_CTI_EVENT,
                            new MapBuilder<String,Object>()
                                    .put("method",Constants.EVENT_SYS_CALL_ON_TIMEOUT)
                                    .put("user_data",call_id)
                                    .build());
                    try {
                        /*发送区域管理器请求次数计数*/
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(sessionContext,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",Constants.EVENT_SYS_CALL_ON_TIMEOUT);
                    }
                }
            });

            /*给CTI发送请求计数*/
            if(sc!=null) sc.getSendCTIRequestCount().incrementAndGet();

            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            e.printStackTrace();
        }
        return response;
    }
}
