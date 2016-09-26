package com.lsxy.area.server.event.handler.simplecall;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.NotifyCall;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.NotifyCallService;
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
public class Handler_EVENT_EXT_NOTIFY_CALL_SUCCESS extends EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_EXT_NOTIFY_CALL_SUCCESS.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    NotifyCallService notifyCallService;
    @Autowired
    CallSessionService callSessionService;
    @Override
    public String getEventName() {
        return Constants.EVENT_EXT_NOTIFY_CALL_SUCCESS;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        String callId = (String)request.getParamMap().get("user_data");
        String resId = (String)request.getParamMap().get("res_id");
        if(StringUtils.isBlank(callId)){
            logger.error("call_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.error("businessstate is null");
            return res;
        }
        if(StringUtils.isNotBlank(resId)){
            state.setResId(resId);
            businessStateService.save(state);
            NotifyCall notifyCall = notifyCallService.findById(callId);
            if(notifyCall != null){
                notifyCall.setResId(resId);
                notifyCallService.save(notifyCall);
            }
            Map<String, Object> data = state.getBusinessData();
            Set<Map.Entry<String, Object>> entries = data.entrySet();
            for(Map.Entry entry:entries){
                String sessionId = (String) entry.getValue();
                CallSession callSession = callSessionService.findById(sessionId);
                if(callSession != null){
                    callSession.setResId(resId);
                    callSessionService.save(callSession);
                }
            }
        }
        return res;
    }
}
