package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCallback;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceCallbackService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Created by liups on 2016/8/31.
 */
@Component
public class Handler_EVENT_EXT_DUO_CALLBACK_SUCCESS extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_DUO_CALLBACK_SUCCESS.class);

    @Autowired
    private BusinessStateService businessStateService;
    @Autowired
    VoiceCallbackService voiceCallbackService;
    @Autowired
    CallSessionService callSessionService;
    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_DUO_CALLBACK_SUCCESS;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String, Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request.params is null");
        }
        if(logger.isDebugEnabled()){
            logger.debug("返回数据map,{}", JSONUtil.objectToJson(params));
        }
        String callId = (String)params.get("user_data1");
        if(StringUtils.isBlank(callId)){
            callId = (String)params.get("user_data2");
            if(StringUtils.isBlank(callId)){
                throw new InvalidParamException("call_id is null");
            }
        }
        String resId = (String)params.get("res_id");
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            throw new InvalidParamException("businessstate is null,call_id={}",callId);
        }
        if(StringUtils.isNotBlank(resId)){
            businessStateService.updateResId(callId,resId);
            VoiceCallback duoCall = voiceCallbackService.findById(callId);
            if(duoCall != null){
                duoCall.setResId(resId);
                voiceCallbackService.save(duoCall);
            }
            Map<String, String> data = state.getBusinessData();
            Set<Map.Entry<String, String>> entries = data.entrySet();
            for(Map.Entry<String,String> entry:entries){
                String sessionId = entry.getValue();
                CallSession callSession = callSessionService.findById(sessionId);
                if(callSession != null){
                    CallSession updateSession = new CallSession();
                    updateSession.setResId(resId);
                    updateSession.setStatus(CallSession.STATUS_CALLING);
                    callSessionService.update(callSession.getId(),updateSession);
                }
            }
        }
        return res;
    }
}
