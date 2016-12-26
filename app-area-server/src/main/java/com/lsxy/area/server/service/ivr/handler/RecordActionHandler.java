package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.RecordFileUtil;
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
 * record指令处理器
 * Created by liuws on 2016/9/2.
 */
@Component
public class RecordActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getAction() {
        return "record";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        String max_duration = root.attributeValue("max_duration");
        String beeping = root.attributeValue("beeping");
        String finish_keys = root.attributeValue("finish_keys");

        String res_id = state.getResId();
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("record_file", RecordFileUtil.getRecordFileUrl(state.getTenantId(),state.getAppId()))
                .putIfNotEmpty("max_seconds",max_duration)
                .putIfNotEmpty("beep",beeping)
                .putIfNotEmpty("finish_keys",finish_keys)
                .putIfNotEmpty("user_data",callId)
                .put("areaId",state.getAreaId())
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_RECORD_START, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
        businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT_FIELD,next);
        return true;
    }
}
