package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
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
public class Handler_MN_CH_SYS_CONF_RECORD_STOP extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CONF_RECORD_STOP.class);

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
        return ServiceConstants.MN_CH_SYS_CONF_RECORD_STOP;
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
            logger.debug("handler process_MN_CH_SYS_CONF_RECORD_STOP:{}",request);
        }

        Map<String, Object> params = request.getParamMap();
        params.put("user_data",request.getParameter("callId"));

        try {
            cticlient.createResource(0, 0, "sys.conf.record_stop", params, null);
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
        }
        return response;

    }
}
