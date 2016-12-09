package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
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
public class Handler_EVENT_SYS_CALL_PLAY_ON_FAIL extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_PLAY_ON_FAIL.class);

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

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_PLAY_ON_FAIL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String call_id = (String)params.get("user_data");

        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }

        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }
        if(canDoivr(state,call_id)){
            ivr(state,params,call_id);
        }
        return res;
    }

    private boolean canDoivr(BusinessState state,String call_id){
        if(BusinessState.TYPE_IVR_CALL.equals(state.getType())){//是ivr呼出
            return true;
        }
        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){//是ivr呼入
            boolean iscc = conversationService.isCC(call_id);
            if(!iscc){//不是ivr呼叫中心呼入
                return true;
            }
            boolean isPlaywait = conversationService.isPlayWait(call_id);
            if(isPlaywait){
                //等待音播放完成需要移除等待音标记
                businessStateService.deleteInnerField(call_id, CallCenterUtil.IS_PLAYWAIT_FIELD);
            }
            if(!isPlaywait){//不是ivr呼叫中心排队
                return true;
            }
        }
        return false;
    }
    private void ivr(BusinessState state,Map<String,Object> params,String call_id){
        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }
        if(StringUtils.isNotBlank(state.getCallBackUrl())){
            ivrActionService.doAction(call_id,new MapBuilder<String,Object>()
                    .putIfNotEmpty("error","play error")
                    .build());
        }
    }
}
