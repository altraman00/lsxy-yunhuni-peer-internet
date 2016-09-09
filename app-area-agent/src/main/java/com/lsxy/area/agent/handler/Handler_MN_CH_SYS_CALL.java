package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.BusAddress;
import com.lsxy.app.area.cti.Commander;
import com.lsxy.area.agent.StasticsCounter;
import com.lsxy.area.agent.cti.CTIClientContext;
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
public class Handler_MN_CH_SYS_CALL extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_SYS_CALL.class);

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
        return ServiceConstants.MN_CH_SYS_CALL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("handler process_MN_CH_SYS_CALL:{}",request);
        }
        RPCResponse response = RPCResponse.buildResponse(request);

        String to = (String) request.getParameter("to");
        String maxAnswerSec = (String) request.getParameter("maxAnswerSec");
        String maxRingSec = (String) request.getParameter("maxRingSec");

        assert  to != null;
        assert maxAnswerSec != null;
        assert maxRingSec!=null;

        Integer iMaxAnswerSec = Integer.parseInt(maxAnswerSec);
        Integer iMaxRingSec = Integer.parseInt(maxRingSec);

        Commander cticlient = cticlientContext.getAvalibleClient();
        if(cticlient == null) {
            response.setMessage(RPCResponse.STATE_EXCEPTION);
            return response;
        }
        try {
            Map<String, Object> params = new HashMap<>();

            params.put("from_uri", "");
            params.put("to_uri", request.getParameter("to") + "@" + ctiHost + ":" + ctiPort);
            params.put("max_answer_seconds", iMaxAnswerSec);
            params.put("max_ring_seconds", iMaxRingSec);

            if(logger.isDebugEnabled()){
                logger.debug("呼叫API调用参数:{}",params);
            }
            //此处临时固定单实例CTI,后期需要抽象CTI路由
            BusAddress ba = new BusAddress((byte)0, (byte)0);
            cticlient.createResource(ba, "sys.call", params, null);

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
