package com.lsxy.area.server.event.handler.call;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.mq.UpdateQueueEvent;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.area.server.util.RecordFileUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.exceptions.api.PlayFileNotExistsException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.ConversationCompletedEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.weaver.ast.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_ON_DIAL_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_DIAL_COMPLETED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterQueueService callCenterQueueService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterAgentService callCenterAgentService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

    @Autowired
    private MQService mqService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_DIAL_COMPLETED;
    }

    /**
     * 拨号结束事件处理
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
        //错误信息。如果拨号失败，该参数记录错误信息。如果拨号成功的被接听，该参数的值是 null。
        String error = (String)params.get("error");

        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id is null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null,call_id="+call_id);
        }
        if(logger.isDebugEnabled()){
            logger.info("call_id={},state={}",call_id,state);
        }
        businessStateService.deleteInnerField(call_id,BusinessState.RINGING_TAG);
        Map<String,String> businessData = state.getBusinessData();

        if(BusinessState.TYPE_SYS_CONF.equals(state.getType())){//该呼叫是通过(会议邀请呼叫)发起需要将呼叫加入会议
            String conf_id = businessData.get("conf_id");
            try {
                if(conf_id == null){
                    throw new InvalidParamException("将呼叫加入到会议失败conf_id为null,state="+state);
                }
                if(StringUtils.isNotBlank(error)){
                    throw new RuntimeException("邀请呼叫加入会议失败"+error+",state="+state);
                }
                confService.confEnter(call_id,conf_id,null,null,null);
            } catch (Throwable e) {
                logger.warn("将呼叫加入到会议失败",e);
                if(StringUtils.isNotBlank(state.getCallBackUrl())){
                    Map<String,Object> notify_data = new MapBuilder<String,Object>()
                            .putIfNotEmpty("event","conf.join.fail")
                            .putIfNotEmpty("id",conf_id)
                            .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                            .putIfNotEmpty("time",System.currentTimeMillis())
                            .putIfNotEmpty("call_id",call_id)
                            .putIfNotEmpty("user_data",state.getUserdata())
                            .build();
                    notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
                }
                hungup(state);
            }
        }else if(BusinessState.TYPE_IVR_CALL.equals(state.getType())){//通过ivr呼出api 发起的呼叫
            //发送拨号结束通知
            Long begin_time = null;
            Long end_time = null;
            if(params.get("begin_time") != null){
                begin_time = (Long.parseLong(params.get("begin_time").toString())) * 1000;
            }
            if(params.get("end_time") != null){
                end_time = (Long.parseLong(params.get("end_time").toString())) * 1000;
            }
            if(StringUtils.isNotBlank(state.getCallBackUrl())){
                Map<String,Object> notify_data = new MapBuilder<String,Object>()
                        .putIfNotEmpty("event","ivr.dial_end")
                        .putIfNotEmpty("id",call_id)
                        .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                        .putIfNotEmpty("begin_time",begin_time)
                        .putIfNotEmpty("end_time",end_time)
                        .putIfNotEmpty("error",error)
                        .putIfNotEmpty("user_data",state.getUserdata())
                        .build();
                notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,null,3);
            }
            if(StringUtils.isBlank(error)){
                if(conversationService.isCC(state)){
                    try{
                        callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(state.getTenantId(),state.getAppId(),
                                new Date()).setCallOutSuccess(1L).build());
                    }catch (Throwable t){
                        logger.error(String.format("incrIntoRedis失败,appId=%s",state.getAppId()),t);
                    }
                }
                ivrActionService.doAction(call_id,null);
            }
        }else if(BusinessState.TYPE_IVR_DIAL.equals(state.getType())){//通过ivr拨号动作发起的呼叫
            String ivr_call_id = businessData.get("ivr_call_id");
            if(StringUtils.isNotBlank(error)){
                Map<String,Object> notify_data = new MapBuilder<String,Object>()
                        .putIfNotEmpty("event","ivr.connect_end")
                        .putIfNotEmpty("id",ivr_call_id)
                        .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                        .putIfNotEmpty("begin_time",System.currentTimeMillis())
                        .putIfNotEmpty("end_time",System.currentTimeMillis())
                        .putIfNotEmpty("error",error)
                        .build();
                notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,null,3);
                ivrActionService.doAction(ivr_call_id,new MapBuilder<String,Object>()
                        .putIfNotEmpty("error","dial error")
                        .build());
            }else{
                BusinessState ivrState = businessStateService.get(ivr_call_id);
                String res_id_one = ivrState.getResId();
                String max_seconds = businessData.get("max_seconds");
                String res_id_two = state.getResId();
                Integer connect_mode = Integer.parseInt(businessData.get("connect_mode"));
                String recording = businessData.get("recording");
                String record_file = null;
                String local_volume = businessData.get("volume1");
                String remote_volume = businessData.get("volume2");
                String schedule_play_time=businessData.get("play_time");
                String schedule_play_file = businessData.get("play_file");
                String schedule_play_loop = businessData.get("play_repeat");
                if(Boolean.parseBoolean(recording)){
                    record_file = RecordFileUtil.getRecordFileUrl(state.getTenantId(),state.getAppId());
                }
                try {
                    schedule_play_file = playFileUtil.convert(state.getTenantId(),state.getAppId(),schedule_play_file);
                    Map<String,Object> map = new MapBuilder<String,Object>()
                            .putIfNotEmpty("res_id",res_id_one)
                            .putIfNotEmpty("max_seconds",max_seconds)
                            .putIfNotEmpty("call_res_id",res_id_two)
                            .putIfNotEmpty("connect_mode",connect_mode)
                            .putIfNotEmpty("record_file",record_file)
                            .putIfNotEmpty("local_volume",local_volume)
                            .putIfNotEmpty("remote_volume",remote_volume)
                            .putIfNotEmpty("schedule_play_time",schedule_play_time)
                            .putIfNotEmpty("schedule_play_file",schedule_play_file)
                            .putIfNotEmpty("schedule_play_loop",schedule_play_loop)
                            .putIfNotEmpty("user_data",ivr_call_id)
                            .build();
                    RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONNECT_START, map);
                    rpcCaller.invoke(sessionContext, rpcrequest);
                    businessStateService.updateInnerField(ivr_call_id,"ivr_dial_call_id",call_id);
                } catch (Throwable e) {
                    hungup(state);
                    Map<String,Object> notify_data = new MapBuilder<String,Object>()
                            .putIfNotEmpty("event","ivr.connect_end")
                            .putIfNotEmpty("id",ivr_call_id)
                            .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                            .putIfNotEmpty("begin_time",System.currentTimeMillis())
                            .putIfNotEmpty("end_time",System.currentTimeMillis())
                            .putIfNotEmpty("error",error)
                            .build();
                    notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,null,3);
                    ivrActionService.doAction(ivr_call_id,new MapBuilder<String,Object>()
                            .putIfNotEmpty("error","dial error")
                            .build());
                }
            }
        }else if(BusinessState.TYPE_CC_INVITE_AGENT_CALL.equals(state.getType())){
            String conversation_id = businessData.get(CallCenterUtil.CONVERSATION_FIELD);
            String agentId = businessData.get(CallCenterUtil.AGENT_ID_FIELD);
            String callCenterId = conversationService.getCallCenter(state);
            if(StringUtils.isNotBlank(error)){
                //呼叫坐席失败
                BusinessState conversationState = businessStateService.get(conversation_id);
                if(conversationState != null){
                    if(conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_STARTED_FIELD) == null){
                        try{
                            CallCenter callCenter = null;
                            if(callCenterId!=null){
                                callCenter = callCenterService.findById(callCenterId);
                            }
                            if(logger.isDebugEnabled()){
                                logger.info("[{}][{}][{}]更新CallCenter,callCenter={},state={}",
                                        state.getTenantId(),state.getAppId(),call_id,callCenter,state);
                            }
                            if(callCenter != null){
                                if(callCenter.getToManualResult() == null){
                                    CallCenter updateCallcenter = new CallCenter();
                                    updateCallcenter.setToManualResult(""+CallCenter.TO_MANUAL_RESULT_AGENT_FAIL);
                                    callCenterService.update(callCenterId,updateCallcenter);
                                }
                            }
                        }catch (Throwable t){
                            logger.warn("更新CallCenter失败",t);
                        }
                    }
                }
                conversationService.exit(conversation_id,call_id);
            }else{
                if(agentId != null){
                    try {
                        String preState = callCenterAgentService.getState(agentId);
                        String curState = CallCenterAgent.STATE_TALKING;
                        if(CallCenterAgent.STATE_FETCHING.equals(preState)){
                            callCenterAgentService.state(state.getTenantId(),state.getAppId(),agentId,curState,true);
                            callCenterUtil.agentStateChangedEvent(state.getSubaccountId(),state.getCallBackUrl(),agentId,
                                    businessData.get(CallCenterUtil.AGENT_NAME_FIELD),preState,curState,state.getUserdata());
                        }
                    } catch (YunhuniApiException e) {
                        logger.info("[{}][{}]agentID={}设置坐席状态失败 ",state.getTenantId(),state.getAppId(),agentId);
                    }
                }
            }
            String initorid = null;
            BusinessState init_state = null;
            String queueId = null;

            //更新排队记录
            initorid = state.getBusinessData().get(CallCenterUtil.INITIATOR_FIELD);
            if(initorid != null){
                init_state = businessStateService.get(initorid);
                if(init_state != null){
                    queueId = init_state.getBusinessData().get(CallCenterUtil.QUEUE_ID_FIELD);
                    if(queueId != null){
                        CallCenterQueue updateQueue = new CallCenterQueue();
                        updateQueue.setDialTime(new Date());
                        updateQueue.setResult(StringUtils.isBlank(error)?CallCenterQueue.RESULT_DIAL_SUCC:CallCenterQueue.RESULT_DIAL_FAIL);
                        try{
                            CallCenterQueue callCenterQueue = callCenterQueueService.findById(queueId);
                            if(callCenterQueue == null){
                                mqService.publish(new UpdateQueueEvent(JSONUtil2.objectToJson(updateQueue),queueId));
                            }else if(callCenterQueue.getDialTime() == null){
                                callCenterQueueService.update(queueId,updateQueue);
                            }
                        }catch (Throwable t){
                            logger.warn(String.format("更新排队记录失败,queueId=%s",queueId),t);
                            mqService.publish(new UpdateQueueEvent(JSONUtil2.objectToJson(updateQueue),queueId));
                        }
                    }
                }
            }

            if(StringUtil.isBlank(error)){
                BusinessState conversationState = businessStateService.get(conversation_id);
                if(conversationState != null){
                    if((conversationState.getClosed()== null || !conversationState.getClosed())){
                        //交谈开始
                        if(conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_STARTED_FIELD) == null){
                            businessStateService.updateInnerField(conversation_id,
                                    CallCenterUtil.CONVERSATION_STARTED_FIELD,CallCenterUtil.CONVERSATION_STARTED_TRUE);
                            //开始录音
                            conversationService.startRecord(conversationState);
                        }

                        //停止交谈播放排队等待音
                        if(conversationService.isPlayWait(conversationState)){
                            conversationService.stopPlay(conversationState.getAreaId(),
                                    conversationState.getId(),conversationState.getResId());
                        }

                        String agent_num = businessData.get(CallCenterUtil.AGENT_NUM_FIELD);
                        String prevoice = businessData.get(CallCenterUtil.AGENT_PRENUMVOICE_FIELD);
                        String postvoice = businessData.get(CallCenterUtil.AGENT_POSTNUMVOICE_FIELD);
                        List<Object[]> plays = new ArrayList<>();
                        try {
                            if(StringUtil.isNotEmpty(prevoice)){
                                plays.add(new Object[]{playFileUtil.convert(state.getTenantId(),state.getAppId(),prevoice),0,""});
                            }
                            if(StringUtil.isNotEmpty(agent_num)){
                                plays.add(new Object[]{agent_num,1,""});
                            }
                            if(StringUtil.isNotEmpty(postvoice)){
                                plays.add(new Object[]{playFileUtil.convert(state.getTenantId(),state.getAppId(),postvoice),0,""});
                            }
                            if(plays!=null && plays.size()>0){
                                Map<String, Object> _params = new MapBuilder<String,Object>()
                                        .putIfNotEmpty("res_id",conversationState.getResId())
                                        .putIfNotEmpty("content", JSONUtil2.objectToJson(plays))
                                        .putIfNotEmpty("user_data",conversation_id)
                                        .put("areaId",state.getAreaId())
                                        .build();
                                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_PLAY, _params);
                                rpcCaller.invoke(sessionContext, rpcrequest);
                            }
                        }catch(PlayFileNotExistsException e){
                            logger.info("[{}][{}]放音文件不存在{},{},{},{}",state.getTenantId(),state.getAppId(),
                                    agent_num,prevoice,postvoice,e);
                        }catch(Throwable e) {
                            logger.warn("调用失败 ",e);
                        }
                        try{
                            CallCenterConversation updateCallCenterConversation = new CallCenterConversation();
                            updateCallCenterConversation.setState(CallCenterConversation.STATE_READY);
                            callCenterConversationService.update(conversation_id,updateCallCenterConversation);
                        }catch (Throwable t){
                            logger.warn("更新交谈记录失败",t);
                        }
                    }

                    if(conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_STARTED_FIELD) == null){
                        try{
                            CallCenter callCenter = null;
                            if(callCenterId!=null){
                                callCenter = callCenterService.findById(callCenterId);
                            }
                            if(logger.isDebugEnabled()){
                                logger.info("[{}][{}][{}]更新CallCenter,callCenter={},state={}",
                                        state.getTenantId(),state.getAppId(),call_id,callCenter,state);
                            }
                            if(callCenter != null){
                                CallCenter updateCallcenter = new CallCenter();
                                if(callCenter.getAnswerTime() == null){
                                    updateCallcenter.setAnswerTime(new Date());
                                }
                                if(callCenter.getToManualTime() != null &&
                                        callCenter.getToManualTimeLong() == null){
                                    Long toManualTimeLong = (new Date().getTime()
                                            - callCenter.getToManualTime().getTime()) / 1000;
                                    updateCallcenter.setToManualTimeLong(toManualTimeLong);
                                }
                                if(callCenter.getToManualResult() == null){
                                    updateCallcenter.setToManualResult(""+CallCenter.TO_MANUAL_RESULT_SUCESS);
                                }
                                if(callCenter.getAgent() == null){
                                    updateCallcenter.setAgent(businessData.get(CallCenterUtil.AGENT_NAME_FIELD));
                                }
                                callCenterService.update(callCenterId,updateCallcenter);
                            }
                        }catch (Throwable t){
                            logger.warn("更新CallCenter失败",t);
                        }
                        try{
                            callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics
                                    .Builder(state.getTenantId(),state.getAppId(),new Date())
                                    .setToManualSuccess(1L)
                                    .build());
                        }catch (Throwable t){
                            logger.error(String.format("incrIntoRedis失败,appId=%s",state.getAppId()),t);
                        }
                        if((conversationState.getClosed()== null || !conversationState.getClosed())){
                            //交谈开始事件
                            callCenterUtil.conversationBeginEvent(conversationState.getSubaccountId(),conversationState.getCallBackUrl(),conversation_id,
                                    conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_TYPE_FIELD),queueId,call_id,conversationState.getUserdata());
                        }
                    }
                }
            }
            if(initorid!= null && init_state != null && queueId!=null){
                if(StringUtil.isNotEmpty(error)){
                    callCenterUtil.sendQueueFailEvent(init_state.getSubaccountId(),init_state.getCallBackUrl(),
                            queueId,
                            init_state.getBusinessData().get(CallCenterUtil.QUEUE_TYPE_FIELD),
                            init_state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                            CallCenterUtil.QUEUE_FAIL_CALLFAIL,
                            initorid,call_id,init_state.getUserdata());
                }else{
                    callCenterUtil.sendQueueSuccessEvent(init_state.getSubaccountId(),init_state.getCallBackUrl(),
                            queueId,
                            init_state.getBusinessData().get(CallCenterUtil.QUEUE_TYPE_FIELD),
                            init_state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                            initorid,call_id,init_state.getUserdata());
                }
            }

        }else if(BusinessState.TYPE_CC_INVITE_OUT_CALL.equals(state.getType())){
            String conversationId = businessData.get(CallCenterUtil.CONVERSATION_FIELD);
            if(conversationId == null){
                logger.info("交谈不存在{}",conversationId);
                return res;
            }
            BusinessState conversationState = businessStateService.get(conversationId);
            if(conversationState == null){
                logger.info("交谈不存在{}",conversationId);
                return res;
            }
            if(conversationState.getClosed() !=null && conversationState.getClosed()){
                logger.info("交谈已结束{}",conversationId);
                return  res;
            }
            String callCenterId = conversationService.getCallCenter(state);

            if(StringUtils.isNotBlank(error)){
                logger.warn("邀请外线加入到交谈失败{}",error);
                if(conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_STARTED_FIELD) == null){
                    try{
                        CallCenter callCenter = null;
                        if(callCenterId!=null){
                            callCenter = callCenterService.findById(callCenterId);
                        }
                        if(logger.isDebugEnabled()){
                            logger.info("[{}][{}][{}]更新CallCenter,callCenter={},state={}",
                                    state.getTenantId(),state.getAppId(),call_id,callCenter,state);
                        }
                        if(callCenter != null){
                            CallCenter updateCallcenter = new CallCenter();
                            if(callCenter.getAnswerTime() == null){
                                updateCallcenter.setAnswerTime(new Date());
                            }
                            if(callCenter.getToManualTime() != null &&
                                    callCenter.getToManualTimeLong() == null){
                                Long toManualTimeLong = (new Date().getTime()
                                        - callCenter.getToManualTime().getTime()) / 1000;
                                updateCallcenter.setToManualTimeLong(toManualTimeLong);
                            }
                            if(callCenter.getToManualResult() == null){
                                updateCallcenter.setToManualResult(""+CallCenter.TO_MANUAL_RESULT_FAIL);
                            }
                            callCenterService.update(callCenterId,updateCallcenter);
                        }
                    }catch (Throwable t){
                        logger.warn("更新CallCenter失败",t);
                    }
                }
                conversationService.logicExit(conversationId,call_id);
            }else{
                //交谈开始
                if(conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_STARTED_FIELD) == null){
                    businessStateService.updateInnerField(conversationId,
                            CallCenterUtil.CONVERSATION_STARTED_FIELD,CallCenterUtil.CONVERSATION_STARTED_TRUE);
                    //开始录音
                    conversationService.startRecord(conversationState);
                    try{
                        CallCenterConversation updateCallCenterConversation = new CallCenterConversation();
                        updateCallCenterConversation.setState(CallCenterConversation.STATE_READY);
                        callCenterConversationService.update(conversationId,updateCallCenterConversation);
                    }catch (Throwable t){
                        logger.warn("更新交谈记录失败",t);
                    }
                    try{
                        CallCenter callCenter = null;
                        if(callCenterId!=null){
                            callCenter = callCenterService.findById(callCenterId);
                        }
                        if(logger.isDebugEnabled()){
                            logger.info("[{}][{}][{}]更新CallCenter,callCenter={},state={}",
                                    state.getTenantId(),state.getAppId(),call_id,callCenter,state);
                        }
                        if(callCenter != null){
                            CallCenter updateCallcenter = new CallCenter();
                            if(callCenter.getAnswerTime() == null){
                                updateCallcenter.setAnswerTime(new Date());
                            }
                            if(callCenter.getToManualTime() != null &&
                                    callCenter.getToManualTimeLong() == null){
                                Long toManualTimeLong = (new Date().getTime()
                                        - callCenter.getToManualTime().getTime()) / 1000;
                                updateCallcenter.setToManualTimeLong(toManualTimeLong);
                            }
                            if(callCenter.getToManualResult() == null){
                                updateCallcenter.setToManualResult(""+CallCenter.TO_MANUAL_RESULT_SUCESS);
                            }
                            callCenterService.update(callCenterId,updateCallcenter);
                        }
                    }catch (Throwable t){
                        logger.warn("更新CallCenter失败",t);
                    }
                    try{
                        callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics
                                .Builder(state.getTenantId(),state.getAppId(),new Date())
                                .setCallOutSuccess(1L)
                                .build());
                    }catch (Throwable t){
                        logger.error(String.format("incrIntoRedis失败,appId=%s",state.getAppId()),t);
                    }
                    //交谈开始事件
                    callCenterUtil.conversationBeginEvent(conversationState.getSubaccountId(),conversationState.getCallBackUrl(),conversationId,
                            conversationState.getBusinessData().get(CallCenterUtil.CONVERSATION_TYPE_FIELD),null,call_id,conversationState.getUserdata());
                }
            }
        }else if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType())){
            if(StringUtils.isNotBlank(error)){
                logger.warn("callid={},呼叫坐席失败{},",call_id,error);
            }else{
                String agentId = businessData.get(CallCenterUtil.AGENT_ID_FIELD);
                if(agentId != null){
                    try {
                        String preState = callCenterAgentService.getState(agentId);
                        String curState = CallCenterAgent.STATE_TALKING;
                        if(CallCenterAgent.STATE_FETCHING.equals(preState)){
                            callCenterAgentService.state(state.getTenantId(),state.getAppId(),agentId,curState,true);
                            callCenterUtil.agentStateChangedEvent(state.getSubaccountId(),state.getCallBackUrl(),agentId,
                                    businessData.get(CallCenterUtil.AGENT_NAME_FIELD),preState,curState,state.getUserdata());
                        }
                    } catch (YunhuniApiException e) {
                        logger.info("[{}][{}]agentID={}设置坐席状态失败 ",state.getTenantId(),state.getAppId(),agentId);
                    }
                }
                String conversationId = businessData.get(CallCenterUtil.CONVERSATION_FIELD);
                if(conversationId == null){
                    return res;
                }
                BusinessState conversationState = businessStateService.get(conversationId);
                if(conversationState !=null ){
                    //直接加入交谈
                    try {
                        conversationService.join(conversationId,call_id,null,null,null);
                    } catch (YunhuniApiException e) {
                        logger.info("加入交谈失败",e);
                        conversationService.logicExit(conversationId,call_id);
                    }
                }else{
                    if(businessData.get("invite_to") != null){
                        try {
                            conversationService.create(state.getSubaccountId(),conversationId,
                                    CallCenterUtil.CONVERSATION_TYPE_CALL_OUT,
                                    state.getBusinessData().get(BusinessState.REF_RES_ID),
                                    state,state.getTenantId(),state.getAppId(),
                                    state.getAreaId(),state.getCallBackUrl(),ConversationService.MAX_DURATION,null,state.getUserdata());
                            //坐席加入交谈成功事件中要呼叫这个号码
                            businessStateService.updateInnerField(conversationId,"invite_from",
                                    businessData.get("invite_from")!=null?businessData.get("invite_from"):"","invite_to",businessData.get("invite_to"));
                            businessStateService.deleteInnerField(call_id,"invite_to","invite_from");
                        } catch (YunhuniApiException e) {
                            conversationService.logicExit(conversationId,call_id);
                        }
                    }else if(businessData.get("enqueue_xml") != null){
                        try {
                            conversationService.create(state.getSubaccountId(),conversationId,
                                    CallCenterUtil.CONVERSATION_TYPE_CALL_AGENT,
                                    state.getBusinessData().get(BusinessState.REF_RES_ID),
                                    state,state.getTenantId(),state.getAppId(),
                                    state.getAreaId(),state.getCallBackUrl(),ConversationService.MAX_DURATION,null,state.getUserdata());
                            //坐席加入交谈成功事件中要呼叫这个号码
                            businessStateService.updateInnerField(conversationId,"enqueue_xml",
                                    businessData.get("enqueue_xml"));
                            businessStateService.deleteInnerField(call_id,"enqueue_xml");
                        } catch (YunhuniApiException e) {
                            conversationService.logicExit(conversationId,call_id);
                        }
                    }
                }
            }
        }
        return res;
    }

    private void hungup(BusinessState state){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("user_data",state.getId())
                .put("areaId",state.getAreaId())
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            if(!businessStateService.closed(state.getId())) {
                rpcCaller.invoke(sessionContext, rpcrequest, true);
            }
        } catch (Throwable e) {
            logger.warn("调用失败",e);
        }
    }
}
