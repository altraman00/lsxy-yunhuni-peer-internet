package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
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
public class Handler_EVENT_SYS_CALL_RECEIVE_DTMF_ON_FAIL extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_RECEIVE_DTMF_ON_FAIL.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private Handler_EVENT_SYS_CALL_ON_RECEIVE_DTMF_COMPLETED handler_event_sys_call_on_receive_dtmf_completed;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_RECEIVE_DTMF_ON_FAIL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            params = new HashedMap();
        }
        params.put("error","receive error");
        RPCRequest.newRequest(request.getName(),params);
        return handler_event_sys_call_on_receive_dtmf_completed.handle(request, session);
    }
}
