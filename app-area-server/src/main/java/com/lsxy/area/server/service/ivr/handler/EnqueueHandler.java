package com.lsxy.area.server.service.ivr.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * enqueue动作指令处理器
 * Created by liuws on 2016/11/16.
 */
@Component
public class EnqueueHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Reference(lazy = true,check = false,timeout = 30000)
    private EnQueueService enQueueService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private DeQueueService deQueueService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private ConversationService conversationService;

    @Override
    public String getAction() {
        return "enqueue";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        Map<String,String> businessData = state.getBusinessData();
        String xml = root.asXML();
        EnQueue enQueue = EnQueueDecoder.decode(xml);
        if(enQueue!=null){
            if(StringUtil.isNotEmpty(enQueue.getData())){
                businessStateService.updateUserdata(callId,enQueue.getData());
            }
            if(StringUtil.isNotBlank(enQueue.getWait_voice())){
                String playWait = enQueue.getWait_voice();
                try {
                    playWait = playFileUtil.convert(state.getTenantId(),state.getAppId(),playWait);
                    if(logger.isDebugEnabled()){
                        logger.debug("[{}][{}]callid={}开始播放排队等待音={}",state.getTenantId(),state.getAppId(),callId, playWait);
                    }
                    Map<String, Object> params = new MapBuilder<String,Object>()
                            .putIfNotEmpty("res_id",state.getResId())
                            .putIfNotEmpty("content", JSONUtil2.objectToJson(new Object[][]{new Object[]{playWait,0,""}}))
                            .putIfNotEmpty("user_data",callId)
                            .put("finish_keys","")//不允许按键打断
                            .put("repeat",10)//
                            .put("areaId",state.getAreaId())
                            .build();
                    RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_PLAY_START, params);
                    if(!businessStateService.closed(callId)){
                        rpcCaller.invoke(sessionContext, rpcrequest);
                    }
                    businessStateService.updateInnerField(callId, CallCenterUtil.PLAYWAIT_FIELD,playWait);
                } catch (Throwable e) {
                    logger.error("调用失败",e);
                }
            }
        }

        businessStateService.updateInnerField(callId,
                CallCenterUtil.ENQUEUE_START_TIME_FIELD,""+new Date().getTime(),
                CallCenterUtil.CHANNEL_ID_FIELD,enQueue.getChannel(),
                CallCenterUtil.CONDITION_ID_FIELD,enQueue.getRoute().getCondition().getId(),
                IVRActionService.IVR_NEXT_FIELD,next);

        String callCenterId = conversationService.getCallCenter(state);
        if(callCenterId != null){
            CallCenter callCenter = callCenterService.findById(callCenterId);
            if(callCenter!=null && callCenter.getToManualTime() == null){//更新转人工时间
                CallCenter updateCallcenter = new CallCenter();
                updateCallcenter.setToManualTime(new Date());
                callCenterService.update(callCenterId,updateCallcenter);
            }
        }

        try {
            enQueueService.lookupAgent(state.getTenantId(), state.getAppId(), businessData.get("to"), callId, enQueue,CallCenterUtil.QUEUE_TYPE_IVR,null);
        }catch (Throwable t){
            logger.error("调用呼叫中心排队失败",t);
            deQueueService.fail(state.getTenantId(),state.getAppId(),callId,null,CallCenterUtil.QUEUE_TYPE_IVR,"调用呼叫中心排队失败",null);
        }
        return true;
    }
}
