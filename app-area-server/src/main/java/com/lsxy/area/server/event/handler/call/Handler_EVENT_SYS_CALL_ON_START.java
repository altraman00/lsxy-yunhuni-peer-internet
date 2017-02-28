package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_START extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_START.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;


    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_START;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String call_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null,call_id=",call_id);
        }
        if(res_id!=null){
            businessStateService.updateResId(call_id,res_id);
            if(state.getBusinessData().get(BusinessState.REF_RES_ID) == null){
                businessStateService.updateInnerField(call_id,BusinessState.REF_RES_ID,res_id);
            }
        }

        if(logger.isDebugEnabled()){
            logger.info("call_id={},state={}",call_id,state);
        }

        //更新会话记录状态
        CallSession callSession = callSessionService.findById(state.getBusinessData().get(BusinessState.SESSIONID));
        if(callSession != null){
            CallSession updateSession = new CallSession();
            updateSession.setResId(res_id);
            updateSession.setStatus(CallSession.STATUS_CALLING);
            callSessionService.update(callSession.getId(),updateSession);
        }
        return res;
    }
}
