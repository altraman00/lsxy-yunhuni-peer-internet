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
public class Handler_EVENT_SYS_CALL_ON_PLAY_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_PLAY_COMPLETED.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private ConversationService conversationService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_PLAY_COMPLETED;
    }

    /**
     * 处理放音结束事件
     * @param request
     * @param session
     * @return
     */
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
        if(canDoivr(state)){
            ivr(state,params,call_id);
        }
        return res;
    }

    private boolean canDoivr(BusinessState state){
        if(!BusinessState.TYPE_IVR_CALL.equals(state.getType())
                && !BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){
            return false;
        }
        boolean iscc = conversationService.isCC(state);
        if(!iscc){//不是ivr呼叫中心
            return true;
        }
        boolean isPlaywait = conversationService.isPlayWait(state.getId());
        if(!isPlaywait){//不是ivr呼叫中心排队放音
            return true;
        }
        //等待音播放完成需要移除等待音标记
        businessStateService.deleteInnerField(state.getId(), CallCenterUtil.IS_PLAYWAIT_FIELD);
        return false;
    }
    private void ivr(BusinessState state,Map<String,Object> params,String call_id){

        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }

        Long begin_time = null;
        Long end_time = null;
        if(params.get("begin_time") != null){
            begin_time = (Long.parseLong(params.get("begin_time").toString())) * 1000;
        }
        if(params.get("end_time") != null){
            end_time = (Long.parseLong(params.get("end_time").toString())) * 1000;
        }
        if(StringUtils.isNotBlank(state.getCallBackUrl())){
            Map<String, Object> notify_data = new MapBuilder<String, Object>()
                    .putIfNotEmpty("event", "ivr.play_end")
                    .putIfNotEmpty("id", call_id)
                    .putIfNotEmpty("begin_time", begin_time)
                    .putIfNotEmpty("end_time", end_time)
                    .putIfNotEmpty("error", params.get("error"))
                    .putIfNotEmpty("key", params.get("finish_key"))
                    .build();
            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,null,3);
        }
        ivrActionService.doAction(call_id,new MapBuilder<String,Object>()
                .putIfNotEmpty("error",params.get("error"))
                .build());
    }
}
