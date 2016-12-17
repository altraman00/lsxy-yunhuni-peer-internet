package com.lsxy.area.server.event.handler.call;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterAgentService callCenterAgentService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

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
            //throw new InvalidParamException("call_id is null");
            logger.info("call_id is null");
            return null;
        }

        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null");
        }
        businessStateService.delete(call_id);

        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }

        //更新会话记录状态
        try{
            CallSession callSession = callSessionService.findById(state.getBusinessData().get(BusinessState.SESSIONID));
            if(callSession != null){
                callSession.setStatus(CallSession.STATUS_OVER);
                callSessionService.save(callSession);
            }
        }catch (Throwable t){
            logger.error("更新会话记录失败",t);
        }
        boolean isIVR = false;

        if(BusinessState.TYPE_IVR_CALL.equals(state.getType())){
            isIVR = true;
        }
        if(BusinessState.TYPE_IVR_DIAL.equals(state.getType())){
            if(!conversationService.isCC(call_id)){
                isIVR = true;
            }
        }
        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){
            if(!conversationService.isCC(call_id)){
                isIVR = true;
            }
        }
        if(isIVR){
            try{
                VoiceIvr voiceIvr = voiceIvrService.findById(call_id);
                if(voiceIvr != null){
                    voiceIvr.setEndTime(new Date());
                    voiceIvrService.save(voiceIvr);
                }
            }catch (Throwable t){
                logger.error("更新voiceIvr失败",t);
            }
        }else{
            try{
                CallCenter callCenter = null;
                if(conversationService.getCallCenter(state)!=null){
                    callCenter = callCenterService.findById(conversationService.getCallCenter(state));
                }
                if(logger.isDebugEnabled()){
                    logger.info("[{}][{}][{}]更新CallCenter,callCenter={}",
                            state.getTenantId(),state.getAppId(),call_id,callCenter);
                }
                if(callCenter != null){
                    callCenter.setEndTime(new Date());
                    Long callLongTime  = null;
                    if(callCenter.getStartTime() != null){
                        callLongTime = (new Date().getTime() - callCenter.getStartTime().getTime()) / 1000;
                        callCenter.setCallTimeLong(callLongTime.toString());
                    }
                    callCenterService.save(callCenter);
                    if(callLongTime != null){
                        try{
                            callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics
                                    .Builder(state.getTenantId(),state.getAppId(),new Date())
                                    .setCallTimeLong(callLongTime)
                                    .build());
                        }catch (Throwable t){
                            logger.error("incrIntoRedis失败",t);
                        }
                    }
                }
            }catch (Throwable t){
                logger.error("更新CallCenter失败",t);
            }
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
            if(StringUtils.isNotBlank(state.getCallBackUrl())){
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
                notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
            }

            String ivr_dial_call_id = null;
            if(state.getBusinessData() != null){
                ivr_dial_call_id = state.getBusinessData().get("ivr_dial_call_id");
            }
            if(StringUtils.isNotBlank(ivr_dial_call_id)){
                hugup(ivr_dial_call_id,state.getAreaId());
            }

            if(conversationService.isCC(call_id)){
                if(logger.isDebugEnabled()){
                    logger.info("[{}][{}]客户挂机callid={}",state.getTenantId(),state.getAppId(),call_id);
                }
                String conversation_id = state.getBusinessData().get(CallCenterUtil.CONVERSATION_FIELD);
                if(conversation_id != null){
                    conversationService.logicExit(conversation_id,call_id);
                }
            }
        }else if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType())){
            String agentId = state.getBusinessData().get(CallCenterUtil.AGENT_ID_FIELD);
            if(agentId != null){
                String preState = null;
                String curState = null;
                try {
                    preState = callCenterAgentService.getState(agentId);
                } catch (YunhuniApiException e) {
                }
                String reserve_state = state.getBusinessData().get(CallCenterUtil.RESERVE_STATE_FIELD);
                try {
                    if(logger.isDebugEnabled()){
                        logger.info("[{}][{}]坐席挂机agentId={}",state.getTenantId(),state.getAppId(),agentId);
                    }
                    callCenterAgentService.state(state.getTenantId(),state.getAppId(),agentId,reserve_state,true);
                } catch (Throwable e) {
                    logger.error("坐席挂机事件设置坐席状态失败agent="+agentId,e);
                }
                try {
                    curState = callCenterAgentService.getState(agentId);
                } catch (YunhuniApiException e) {
                }
                if(preState!=null && curState != null){
                    if(!preState.equals(curState)){
                        callCenterUtil.agentStateChangedEvent(state.getCallBackUrl(),agentId,preState, curState);
                    }
                }
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
