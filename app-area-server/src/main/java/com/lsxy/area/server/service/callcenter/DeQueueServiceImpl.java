package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class DeQueueServiceImpl implements DeQueueService {

    private static final Logger logger = LoggerFactory.getLogger(DeQueueServiceImpl.class);

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterQueueService callCenterQueueService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

    /**
     * 创建交谈，然后呼叫坐席。
     * 交谈创建成功事件邀请排队的客户到交谈，如果是在交谈上发起排队就忽略这一步
     * 开始呼叫事件中，将坐席加入交谈
     * 坐席拨号完成事件中，播放工号
     * 客户和坐席开始交谈
     * @param tenantId
     * @param appId
     * @param callId   发起排队的呼叫id或交谈id
     * @param queueId
     * @param result
     * @param conversationId 不为空代表邀请坐席到一个存在的交谈
     * @throws Exception
     */
    @Override
    public void success(String tenantId, String appId, String callId,String queueType,
                        String queueId, EnQueueResult result,String conversationId) throws Exception{
        if(logger.isDebugEnabled()){
            logger.debug("排队成功，tenantId={},appId={},callId={},queueId={},result={}",
                    tenantId,appId,callId,queueId,result);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || (state.getClosed() != null && state.getClosed())){
            logger.info("会话已关闭callid={}",callId);
            //抛异常后呼叫中心微服务会回滚坐席状态
            throw new IllegalStateException("会话已关闭");
        }
        conversationService.stopPlayWait(state);
        String conversation = conversationId;

        if(conversation == null){
            conversation = UUIDGenerator.uuid();
        }

        //更新排队的call的所属交谈id和排队id
        businessStateService.updateInnerField(callId,
                CallCenterUtil.CONVERSATION_FIELD,conversation,
                CallCenterUtil.QUEUE_ID_FIELD,queueId,
                CallCenterUtil.QUEUE_TYPE_FIELD,queueType);

        BaseEnQueue enQueue = result.getBaseEnQueue();
        if(enQueue == null){
            enQueue = conversationService.getEnqueue(queueId);
        }
        Integer conversationTimeout = enQueue.getConversation_timeout();

        //开始呼叫坐席
        String agentCallId = conversationService.inviteAgent
                (appId,state.getBusinessData().get(BusinessState.REF_RES_ID),state.getId(),conversation,result.getAgent().getId(),
                        result.getAgent().getName(),result.getExtension().getId(),
                        state.getBusinessData().get("from"),state.getBusinessData().get("to"),
                        result.getExtension().getTelnum(),result.getExtension().getType(),
                        result.getExtension().getUser(),conversationTimeout,result.getFetchTime(),enQueue.getVoice_mode());
        if(conversationId == null){//在现有的交谈上邀请座席，不需要创建交谈
            //开始创建交谈
            conversationService.create(conversation,state.getBusinessData().get(BusinessState.REF_RES_ID),enQueue.getChannel(),
                    state,state.getTenantId(),state.getAppId(),state.getAreaId(),state.getCallBackUrl(),conversationTimeout,enQueue.getHold_voice());
        }

        if(state.getBusinessData().get(CallCenterUtil.PLAYWAIT_FIELD) != null){
            businessStateService.updateInnerField(conversation,CallCenterUtil.PLAYWAIT_FIELD,state.getBusinessData().get(CallCenterUtil.PLAYWAIT_FIELD));
        }
        //设置坐席的businessstate
        setAgentState(agentCallId,enQueue,result);

        //更新排队结果
        updateQueue(queueId,callId,conversation,result.getAgent().getName(),agentCallId,CallCenterQueue.RESULT_SELETEED);

        if(conversationService.isCC(state)){
            try{
                //更新呼叫中心统计数据
                callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics
                        .Builder(state.getTenantId(),state.getAppId(),new Date())
                        .setQueueNum(1L)
                        .setQueueDuration((System.currentTimeMillis()
                                - Long.parseLong(state.getBusinessData()
                                .get(CallCenterUtil.ENQUEUE_START_TIME_FIELD)))/1000)
                        .build());
            }catch (Throwable t){
                logger.error("incrIntoRedis失败",t);
            }
        }

        callCenterUtil.sendQueueSelectedAgentEvent(state.getCallBackUrl(),
                queueId,queueType,
                state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD),
                state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                callId,agentCallId,state.getUserdata());

        callCenterUtil.agentStateChangedEvent(state.getCallBackUrl(),result.getAgent().getId(),
                result.getAgent().getName(),
                CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING);
    }

    @Override
    public void timeout(String tenantId, String appId, String callId,String queueId,String queueType,String conversationId) {
        if(logger.isDebugEnabled()){
            logger.debug("排队超时,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);

        if(state != null){
            if(conversationService.isCC(state)) {
                updateCallCenterTOMANUALRESULT(state, "" + CallCenter.TO_MANUAL_RESULT_TIME_OUT);
                try {
                    callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics
                            .Builder(state.getTenantId(), state.getAppId(), new Date())
                            .setQueueNum(1L)
                            .setQueueDuration((System.currentTimeMillis() - Long.parseLong(state.getBusinessData()
                                    .get(CallCenterUtil.ENQUEUE_START_TIME_FIELD))) / 1000)
                            .build());
                } catch (Throwable t) {
                    logger.error("incrIntoRedis失败", t);
                }
            }
            updateQueue(queueId,callId,null,null,null,CallCenterQueue.RESULT_FAIL);
        }


        if(conversationId != null){//在交谈上排队 需要停止播放交谈排队等待音
            BusinessState conversationState = businessStateService.get(conversationId);
            if(conversationState != null && conversationState.getResId() != null &&
                    (conversationState.getClosed() == null || !conversationState.getClosed()) &&
                    conversationService.isPlayWait(conversationState)){
                conversationService.stopPlay(conversationState.getAreaId(),
                        conversationState.getId(),conversationState.getResId());
            }
        }

        if(state == null || (state.getClosed() != null && state.getClosed())){
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        conversationService.stopPlayWait(state);

        callCenterUtil.sendQueueFailEvent(state.getCallBackUrl(),
                queueId,queueType,
                state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD),
                state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                CallCenterUtil.QUEUE_FAIL_TIMEOUT,
                callId,null,state.getUserdata());

        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType()) ||
                BusinessState.TYPE_IVR_CALL.equals(state.getType())){
            ivrActionService.doAction(callId,new MapBuilder<String,Object>()
                    .put("error",CallCenterUtil.QUEUE_FAIL_TIMEOUT).build());
        }
    }

    @Override
    public void fail(String tenantId, String appId, String callId,String queueId,String queueType, String reason,String conversationId) {
        if(logger.isDebugEnabled()){
            logger.debug("排队失败,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);

        if(state != null){
            if(conversationService.isCC(state)){
                updateCallCenterTOMANUALRESULT(state,""+CallCenter.TO_MANUAL_RESULT_FAIL);
                try{
                    callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics
                            .Builder(state.getTenantId(),state.getAppId(),new Date())
                            .setQueueNum(1L)
                            .setQueueDuration((System.currentTimeMillis()
                                    - Long.parseLong(state.getBusinessData()
                                    .get(CallCenterUtil.ENQUEUE_START_TIME_FIELD)))/1000)
                            .build());
                }catch (Throwable t){
                    logger.error("incrIntoRedis失败",t);
                }
            }
            updateQueue(queueId,callId,null,null,null,CallCenterQueue.RESULT_FAIL);
        }


        if(conversationId != null){//在交谈上排队 需要停止播放交谈排队等待音
            BusinessState conversationState = businessStateService.get(conversationId);
            if(conversationState != null && conversationState.getResId() != null &&
                    (conversationState.getClosed() == null || !conversationState.getClosed()) &&
                    conversationService.isPlayWait(conversationState)){
                conversationService.stopPlay(conversationState.getAreaId(),
                        conversationState.getId(),conversationState.getResId());
            }
        }

        if(state == null || (state.getClosed() != null && state.getClosed())){ 
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        conversationService.stopPlayWait(state);
        
        callCenterUtil.sendQueueFailEvent(state.getCallBackUrl(),
                queueId,queueType,
                state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD),
                state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                CallCenterUtil.QUEUE_FAIL_ERROR,
                callId,null,state.getUserdata());

        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType()) ||
                BusinessState.TYPE_IVR_CALL.equals(state.getType())){
            ivrActionService.doAction(callId,new MapBuilder<String,Object>()
                    .put("error",CallCenterUtil.QUEUE_FAIL_ERROR).build());
        }
    }

    /**
     * 更新排队结果
     * @param id
     * @param callId
     * @param conversationId
     * @param agentName
     * @param agentCallId
     * @param result
     */
    private void updateQueue(String id,String callId,String conversationId,
                             String agentName,String agentCallId,String result){
        //更新排队记录
        try{
            if(id == null){
                return;
            }
            CallCenterQueue callCenterQueue = callCenterQueueService.findById(id);
            if(callCenterQueue == null){
                return;
            }
            CallCenterQueue updateCallCenterQueue = new CallCenterQueue();

            Date cur = new Date();
            updateCallCenterQueue.setRelevanceId(callId);
            if(conversationId != null){
                updateCallCenterQueue.setConversation(conversationId);
            }
            if(agentName != null){
                updateCallCenterQueue.setAgent(agentName);
            }
            if(agentCallId != null){
                updateCallCenterQueue.setAgentCallId(agentCallId);
                updateCallCenterQueue.setInviteTime(cur);
            }
            updateCallCenterQueue.setEndTime(cur);
            if(callCenterQueue.getStartTime()!=null){
                updateCallCenterQueue.setToManualTime((cur.getTime() - callCenterQueue.getStartTime().getTime()) / 1000);
            }
            updateCallCenterQueue.setResult(result);
            callCenterQueueService.update(id,updateCallCenterQueue);
        }catch (Throwable t){
            logger.error("更新排队记录失败",t);
        }
    }

    public void updateCallCenterTOMANUALRESULT(BusinessState state,String result){
        try{
            String callCenterId = conversationService.getCallCenter(state);
            CallCenter callCenter = null;
            if(callCenterId!=null){
                callCenter = callCenterService.findById(callCenterId);
            }
            if(callCenter != null){
                if(callCenter.getToManualResult() == null){
                    CallCenter updateCallcenter = new CallCenter();
                    updateCallcenter.setToManualResult(result);
                    callCenterService.update(callCenterId,updateCallcenter);
                }
            }
        }catch (Throwable t){
            logger.error("更新CallCenter失败",t);
        }
    }

    private void setAgentState(String agentCallId, BaseEnQueue enQueue,EnQueueResult result){
        List<String> innerFields = new ArrayList<>();
        String reserveState = enQueue.getReserve_state();
        boolean playNum = enQueue.isPlay_num();
        String preNumVoice = enQueue.getPre_num_voice();
        String postNumVoice = enQueue.getPost_num_voice();
        if(StringUtil.isNotEmpty(reserveState)){
            innerFields.add(CallCenterUtil.RESERVE_STATE_FIELD);
            innerFields.add(reserveState);
        }
        if(playNum){
            if(StringUtil.isNotEmpty(result.getAgent().getNum())){
                innerFields.add(CallCenterUtil.AGENT_NUM_FIELD);
                innerFields.add(result.getAgent().getNum());
            }
            if(StringUtil.isNotEmpty(preNumVoice)){
                innerFields.add(CallCenterUtil.AGENT_PRENUMVOICE_FIELD);
                innerFields.add(preNumVoice);
            }
            if(StringUtil.isNotEmpty(postNumVoice)){
                innerFields.add(CallCenterUtil.AGENT_POSTNUMVOICE_FIELD);
                innerFields.add(postNumVoice);
            }
        }
        if(innerFields.size()>0){
            businessStateService.updateInnerField(agentCallId,innerFields);
        }
    }
}
