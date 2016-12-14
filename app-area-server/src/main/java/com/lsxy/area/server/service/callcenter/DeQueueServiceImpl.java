package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.BaseEnQueue;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.model.EnQueueResult;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.framework.core.utils.MapBuilder;
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

import java.util.Date;
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

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

    /**
     * 创建交谈，然后呼叫坐席。
     * 交谈创建成功事件邀请排队的客户到交谈
     * 开始呼叫事件中，将坐席加入交谈
     * 坐席拨号完成事件中，播放工号
     * 客户和坐席开始交谈
     * @param tenantId
     * @param appId
     * @param callId
     * @param queueId
     * @param result
     * @throws Exception
     */
    @Override
    public void success(String tenantId, String appId, String callId,
                        String queueId, EnQueueResult result) throws Exception{
        if(logger.isDebugEnabled()){
            logger.debug("排队成功，tenantId={},appId={},callId={},queueId={},result=",
                    tenantId,appId,callId,queueId,result);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || (state.getClosed() != null && state.getClosed())){
            logger.info("会话已关闭callid={}",callId);
            //抛异常后呼叫中心微服务会回滚坐席状态
            throw new IllegalStateException("会话已关闭");
        }
        String conversation = UUIDGenerator.uuid();
        stopPlayWait(state.getAreaId(),state.getId(),state.getResId());

        businessStateService.updateInnerField(callId,2,
                CallCenterUtil.CONVERSATION_FIELD,CallCenterUtil.QUEUE_ID_FIELD,conversation,queueId);

        BaseEnQueue enQueue = result.getBaseEnQueue();
        if(enQueue == null){
            enQueue = conversationService.getEnqueue(queueId);
        }
        Integer conversationTimeout = enQueue.getConversation_timeout();
        String reserveState = enQueue.getReserve_state();
        boolean playNum = enQueue.isPlay_num();
        String preNumVoice = enQueue.getPre_num_voice();
        String postNumVoice = enQueue.getPost_num_voice();

        String agentCallId = conversationService.inviteAgent(appId,conversation,result.getAgent().getId(),
                result.getAgent().getName(),result.getExtension().getId(),
                result.getExtension().getTelnum(),result.getExtension().getType(),
                result.getExtension().getUser(),conversationTimeout,45);

        conversationService.create(conversation,state.getId(),state.getTenantId(),
                state.getAppId(),state.getAreaId(),state.getCallBackUrl(),conversationTimeout);


        if(reserveState != null){
            businessStateService.updateInnerField(agentCallId,CallCenterUtil.RESERVE_STATE_FIELD,reserveState);
        }
        if(playNum){
            if(result.getAgent().getNum() != null){
                businessStateService.updateInnerField(agentCallId,CallCenterUtil.AGENT_NUM_FIELD,result.getAgent().getNum());
            }
            if(preNumVoice != null){
                businessStateService.updateInnerField(agentCallId,CallCenterUtil.AGENT_PRENUMVOICE_FIELD,preNumVoice);
            }
            if(postNumVoice != null){
                businessStateService.updateInnerField(agentCallId,CallCenterUtil.AGENT_POSTNUMVOICE_FIELD,postNumVoice);
            }
        }
        updateQueue(queueId,callId,conversation,result.getAgent().getId(),agentCallId,CallCenterQueue.RESULT_SELETEED);

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

        callCenterUtil.sendQueueSelectedAgentEvent(state.getCallBackUrl(),
                queueId,CallCenterUtil.QUEUE_TYPE_IVR,
                state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD),
                state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                callId,agentCallId,state.getUserdata());

        callCenterUtil.agentStateChangedEvent(state.getCallBackUrl(),result.getAgent().getId(), CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING);
    }

    @Override
    public void timeout(String tenantId, String appId, String callId,String queueId) {
        if(logger.isDebugEnabled()){
            logger.debug("排队超时,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || (state.getClosed() != null && state.getClosed())){
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        stopPlayWait(state.getAreaId(),state.getId(),state.getResId());

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

        callCenterUtil.sendQueueFailEvent(state.getCallBackUrl(),
                queueId,CallCenterUtil.QUEUE_TYPE_IVR,
                state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD),
                state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                CallCenterUtil.QUEUE_FAIL_TIMEOUT,
                callId,null,state.getUserdata());

        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){
            ivrActionService.doAction(callId,new MapBuilder<String,Object>()
                    .put("error",CallCenterUtil.QUEUE_FAIL_TIMEOUT).build());
        }
        updateQueue(queueId,callId,null,null,null,CallCenterQueue.RESULT_FAIL);
    }

    @Override
    public void fail(String tenantId, String appId, String callId,String queueId, String reason) {
        if(logger.isDebugEnabled()){
            logger.debug("排队失败,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || (state.getClosed() != null && state.getClosed())){ 
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        stopPlayWait(state.getAreaId(),state.getId(),state.getResId());

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

        callCenterUtil.sendQueueFailEvent(state.getCallBackUrl(),
                queueId,CallCenterUtil.QUEUE_TYPE_IVR,
                state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD),
                state.getBusinessData().get(CallCenterUtil.CONDITION_ID_FIELD),
                CallCenterUtil.QUEUE_FAIL_ERROR,
                callId,null,state.getUserdata());

        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){
            ivrActionService.doAction(callId,new MapBuilder<String,Object>()
                    .put("error",CallCenterUtil.QUEUE_FAIL_ERROR).build());
        }
        updateQueue(queueId,callId,null,null,null,CallCenterQueue.RESULT_FAIL);
    }

    /**
     * 更新排队结果
     * @param id
     * @param callId
     * @param conversationId
     * @param agentId
     * @param agentCallId
     * @param result
     */
    private void updateQueue(String id,String callId,String conversationId,
                             String agentId,String agentCallId,String result){
        //更新排队记录
        try{
            if(id == null){
                return;
            }
            CallCenterQueue callCenterQueue = callCenterQueueService.findById(id);
            if(callCenterQueue == null){
                return;
            }
            Date cur = new Date();
            callCenterQueue.setRelevanceId(callId);
            if(conversationId != null){
                callCenterQueue.setConversation(conversationId);
            }
            if(agentId != null){
                callCenterQueue.setAgent(agentId);
            }
            if(agentCallId != null){
                callCenterQueue.setAgentCallId(agentCallId);
                callCenterQueue.setInviteTime(cur);
            }
            callCenterQueue.setEndTime(cur);
            callCenterQueue.setToManualTime((callCenterQueue.getEndTime().getTime() - callCenterQueue.getStartTime().getTime()) / 1000);
            callCenterQueue.setResult(result);
            callCenterQueueService.save(callCenterQueue);
        }catch (Throwable t){
            logger.error("更新排队记录失败",t);
        }
    }

    /**
     * 停止播放排队等待音
     * @param area_id
     * @param call_id
     * @param res_id
     */
    private void stopPlayWait(String area_id,String call_id,String res_id){
        try {
            if(conversationService.isPlayWait(call_id)){
                    Map<String, Object> params = new MapBuilder<String,Object>()
                            .putIfNotEmpty("res_id",res_id)
                            .putIfNotEmpty("user_data",call_id)
                            .put("areaId",area_id)
                            .build();
                    RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_PLAY_STOP, params);
                    rpcCaller.invoke(sessionContext, rpcrequest);
            }
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
    }
}
