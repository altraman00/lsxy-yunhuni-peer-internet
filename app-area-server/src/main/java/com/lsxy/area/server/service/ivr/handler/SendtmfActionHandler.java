package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 发码指令处理器
 * Created by liuws on 2016/9/2.
 */
@Component
public class SendtmfActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getAction() {
        return "send_dtmf";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        String dtmf_code = root.getTextTrim();

        String res_id = state.getResId();
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("keys",dtmf_code)
                .putIfNotEmpty("user_data",callId)
                .put("areaId",state.getAreaId())
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_SEND_DTMF_START, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
        businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT_FIELD,next);
        return true;
    }
}
