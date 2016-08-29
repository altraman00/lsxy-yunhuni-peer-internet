package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcError;
import com.lsxy.app.area.cti.commander.RpcResultListener;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
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
public class Handler_MN_CH_SYS_CONF extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CONF.class);

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
        return ServiceConstants.MN_CH_SYS_CONF;
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
            logger.debug("handler process_MN_CH_SYS_CONF:{}",request);
        }

        Map<String, Object> params = request.getParamMap();
        String conf_id = (String)request.getParameter("callId");

        params.put("user_data",conf_id);

        try {
            cticlient.createResource(0, 0, "sys.conf", params, new RpcResultListener(){
                @Override
                protected void onResult(Object o) {

                    if(logger.isDebugEnabled()){
                        logger.debug("资源{}[{}={}]创建成功",getEventName(),conf_id,o);
                    }

                    String res_id = o.toString();

                    RPCRequest req = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_ON_START,
                            String.format("res_id=%s&conf_id=%s",res_id,conf_id));
                    Session session = sessionContext.getAvalibleSession();
                    try {
                        /*发送区域管理器请求次数计数*/
                        if(sc!=null) sc.getSendAreaServerRequestCount().incrementAndGet();
                        rpcCaller.invoke(session,req);
                    } catch (Exception e) {
                        logger.error("CTI发送事件%s,失败",ServiceConstants.MN_CH_SYS_CONF_ON_START);
                    }
                }

                @Override
                protected void onError(RpcError rpcError) {
                    logger.error("资源{}[{}]创建失败:{}",getEventName(),conf_id,rpcError);
                }

                @Override
                protected void onTimeout() {
                    logger.error("资源{}[{}]创建超时",getEventName(),conf_id);
                }
            });
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
        }
        return response;
    }
}
