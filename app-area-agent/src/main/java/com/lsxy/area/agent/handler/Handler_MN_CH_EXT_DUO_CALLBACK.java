package com.lsxy.area.agent.handler;

import com.lsxy.app.area.cti.commander.Client;
import com.lsxy.app.area.cti.commander.RpcResultListener;
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
public class Handler_MN_CH_EXT_DUO_CALLBACK extends RpcRequestHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_EXT_DUO_CALLBACK.class);

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
        //TODO 获取线路IP和端口
        Map<String, Object> params = new HashMap<>();
        params.put("from1_uri", request.getParameter("from1")+"@"+ctiHost+":"+ctiPort);
        params.put("to1_uri", request.getParameter("to1")+"@"+ctiHost+":"+ctiPort);
        params.put("from2_uri", request.getParameter("from2")+"@"+ctiHost+":"+ctiPort);
        params.put("to2_uri",request.getParameter("to2")+"@"+ctiHost+":"+ctiPort);
        params.put("max_connect_seconds",Integer.parseInt((String) request.getParameter("max_call_duration")));
        params.put("max_ring_seconds",Integer.parseInt((String) request.getParameter("max_dial_duration")));
        params.put("ring_play_file",request.getParameter("ring_tone"));
        params.put("ring_play_mode",Integer.parseInt((String) request.getParameter("ring_tone_mode")));
        params.put("user_data",request.getParameter("callId"));
        //录音
        Boolean recording = new Boolean((String) request.getParameter("recording"));
        if(recording){
            //TODO 录音文件名称
            params.put("record_file ",request.getParameter("callId"));
            params.put("record_mode",Integer.parseInt((String) request.getParameter("record_mode")));
            params.put("record_format ",1);
        }

        try {
            if(logger.isDebugEnabled()){
                logger.debug("调用CTI创建双向回拔资源，参数为{}", JSONUtil.objectToJson(params));
            }
            cticlient.createResource(0, 0, "ext.duo_callback", params,null);
            response.setMessage(RPCResponse.STATE_OK);
        } catch (IOException e) {
            logger.error("操作CTI资源异常{}",request);
            e.printStackTrace();
        }

        return response;
    }
}
