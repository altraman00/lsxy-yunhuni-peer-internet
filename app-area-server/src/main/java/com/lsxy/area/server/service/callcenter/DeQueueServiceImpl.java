package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.BaseEnQueue;
import com.lsxy.call.center.api.model.EnQueueResult;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
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
        if(state == null || state.getClosed()){
            logger.info("会话已关闭callid={}",callId);
            //抛异常后呼叫中心微服务会回滚坐席状态
            throw new IllegalStateException("会话已关闭");
        }
        stopPlayWait(state.getAreaId(),state.getId(),state.getResId());
        Map<String,Object> businessData = state.getBusinessData();
        if(businessData == null){
            businessData = new HashMap<>();
            state.setBusinessData(businessData);
        }
        businessData.put(ConversationService.QUEUE_ID_FIELD,queueId);
        businessStateService.save(state);

        BaseEnQueue enQueue = conversationService.getEnqueue(queueId);
        Integer conversationTimeout = enQueue.getConversation_timeout();
        String reserveState = enQueue.getReserve_state();
        boolean playNum = enQueue.isPlay_num();
        String preNumVoice = enQueue.getPre_num_voice();
        String postNumVoice = enQueue.getPost_num_voice();
        String conversation = conversationService.create(state.getId(),
                (String)businessData.get(ConversationService.CALLCENTER_ID_FIELD),state.getAppId(),conversationTimeout);
        String agentCallId = conversationService.inviteAgent(appId,conversation,result.getAgent().getId(),
                result.getExtension().getTelenum(),result.getExtension().getType(),
                result.getExtension().getUser(),conversationTimeout,45);

        BusinessState agentState = businessStateService.get(agentCallId);
        if(reserveState != null){
            agentState.getBusinessData().put(ConversationService.RESERVE_STATE_FIELD,reserveState);
        }
        if(playNum){
            agentState.getBusinessData().put(ConversationService.AGENT_NUM_FIELD,result.getAgent().getNum());
            agentState.getBusinessData().put(ConversationService.AGENT_PRENUMVOICE_FIELD,preNumVoice);
            agentState.getBusinessData().put(ConversationService.AGENT_POSTNUMVOICE_FIELD,postNumVoice);
        }
        if(reserveState != null || playNum){
            businessStateService.save(agentState);
        }
    }

    @Override
    public void timeout(String tenantId, String appId, String callId) {
        if(logger.isDebugEnabled()){
            logger.debug("排队超时,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || state.getClosed()){
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        stopPlayWait(state.getAreaId(),state.getId(),state.getResId());
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .putIfNotEmpty("event","callcenter.enqueue.timeout")
                .putIfNotEmpty("id",callId)
                .putIfNotEmpty("user_data",state.getUserdata())
                .build();
        if(notifyCallbackUtil.postNotifySync(state.getCallBackUrl(),notify_data,null,3)){
            ivrActionService.doAction(callId);
        }
    }

    @Override
    public void fail(String tenantId, String appId, String callId, String reason) {
        if(logger.isDebugEnabled()){
            logger.debug("排队失败,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || state.getClosed()){
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        stopPlayWait(state.getAreaId(),state.getId(),state.getResId());
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .putIfNotEmpty("event","callcenter.enqueue.fail")
                .putIfNotEmpty("id",callId)
                .putIfNotEmpty("user_data",state.getUserdata())
                .build();
        if(notifyCallbackUtil.postNotifySync(state.getCallBackUrl(),notify_data,null,3)){
            ivrActionService.doAction(callId);
        }
    }

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
