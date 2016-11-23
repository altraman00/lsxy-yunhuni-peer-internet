package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
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
public class Handler_EVENT_SYS_CALL_ON_RELEASE extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_RELEASE.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_RELEASE;
    }

    /**
     * 处理呼叫结束事件
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
        businessStateService.delete(call_id);
        if(StringUtils.isBlank(state.getAppId())){
            throw new InvalidParamException("没有找到对应的app信息appId={}",state.getAppId());
        }
        App app = appService.findById(state.getAppId());
        if(app == null){
            throw new InvalidParamException("没有找到对应的app信息appId={}",state.getAppId());
        }

        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }

        //更新会话记录状态
        CallSession callSession = callSessionService.findById((String)state.getBusinessData().get("sessionid"));
        if(callSession != null){
            callSession.setStatus(CallSession.STATUS_OVER);
            callSessionService.save(callSession);
        }

        //如果ivr主动方挂断，需要同时挂断正在连接的呼叫
        if(BusinessState.TYPE_IVR_CALL.equals(state.getType()) ||
            BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){
            Long begin_time = null;
            Long end_time = null;
            Long answer_time = null;
            if(params.get("begin_time") != null){
                begin_time = (Long.parseLong(params.get("begin_time").toString())) * 1000;
            }
            if(params.get("end_time") != null){
                end_time = (Long.parseLong(params.get("end_time").toString())) * 1000;
            }
            if(params.get("answer_time") != null){
                answer_time = (Long.parseLong(params.get("answer_time").toString())) * 1000;
            }
            //发送呼叫结束通知
            if(StringUtils.isNotBlank(app.getUrl())){
                Map<String,Object> notify_data = new MapBuilder<String,Object>()
                        .putIfNotEmpty("event","ivr.call_end")
                        .putIfNotEmpty("id",call_id)
                        .putIfNotEmpty("begin_time",begin_time)
                        .putIfNotEmpty("answer_time",answer_time)
                        .putIfNotEmpty("end_time",end_time)
                        .putIfNotEmpty("end_by",params.get("dropped_by"))
                        .putIfNotEmpty("cause",params.get("cause"))
                        .putIfNotEmpty("user_data",state.getUserdata())
                        .build();
                notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
            }

            String ivr_dial_call_id = null;
            if(state.getBusinessData() != null){
                ivr_dial_call_id = (String)state.getBusinessData().get("ivr_dial_call_id");
            }
            if(StringUtils.isNotBlank(ivr_dial_call_id)){
                hugup(ivr_dial_call_id,state.getAreaId());
            }
        }else if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType())){
            //TODO 坐席状态更改
            String agentId = (String)state.getBusinessData().get(ConversationService.AGENT_ID_FIELD);
            if(agentId != null){
                String reserve_state = (String)state.getBusinessData().get(ConversationService.RESERVE_STATE_FIELD);
                
            }
        }
        return res;
    }

    public void hugup(String ivr_dial_call_id,String areaId){
        BusinessState state_dial = businessStateService.get(ivr_dial_call_id);
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state_dial.getResId())
                .putIfNotEmpty("user_data",ivr_dial_call_id)
                .put("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
    }
}
