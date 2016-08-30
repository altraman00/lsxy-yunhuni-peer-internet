package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
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
public class Handler_MN_CH_EXT_NOTIFY_CALL extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_EXT_NOTIFY_CALL.class);

    @Value("${area.agent.client.cti.sip.host}")
    private String ctiHost;

    @Value("${area.agent.client.cti.sip.port}")
    private int ctiPort;

    @Autowired
    private CTIClientContext cticlientContext;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_EXT_NOTIFY_CALL;
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
            logger.debug("handler process_MN_CH_EXT_NOTIFY_CALL:{}",request);
        }
        //TODO 获取线路IP和端口
        Map<String, Object> params = new HashMap<>();
        params.put("from_uri", request.getParameter("from")+"@"+ctiHost+":"+ctiPort);
        params.put("to_uri", request.getParameter("to")+"@"+ctiHost+":"+ctiPort);
        params.put("play_content",Integer.parseInt((String) request.getParameter("files")));
        params.put("play_repeat",Integer.parseInt((String) request.getParameter("repeat")));
        params.put("max_ring_seconds",request.getParameter("max_dial_duration"));
        params.put("user_data",request.getParameter("callId"));

        try {
            if(logger.isDebugEnabled()){
                logger.debug("调用CTI创建双向回拔资源，参数为{}", JSONUtil.objectToJson(params));
            }
            String res_id = cticlient.createResource(0, 0, "ext.notify_call", params, null);
            response.setMessage(RPCResponse.STATE_OK);
            response.setBody(res_id);
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
            e.printStackTrace();
        }

        return response;
    }
}
