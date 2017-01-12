package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.callcenter.AgentIdCallReference;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationCallVoiceModeReference;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by liuws on 2017/1/9.
 */
@Service
@Component
public class ConversationOps implements com.lsxy.area.api.callcenter.ConversationOps{

    public static final Logger logger = LoggerFactory.getLogger(ConversationOps.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    private AgentIdCallReference agentIdCallReference;

    @Autowired
    private IVRActionService ivrActionService;

    @Reference(lazy = true,check = false,timeout = 30000)
    private EnQueueService enQueueService;

    @Autowired
    private DeQueueService deQueueService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private ConversationCallVoiceModeReference conversationCallVoiceModeReference;

    @Override
    public boolean dismiss(String ip, String appId, String conversationId) throws YunhuniApiException{
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        return conversationService.dismiss(appId, conversationId);
    }

    @Override
    public boolean setVoiceMode(String ip, String appId, String conversationId, String agentId, Integer voiceMode) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(agentId)){
            throw new RequestIllegalArgumentException();
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException();
        }
        return conversationService.setVoiceMode(conversationId,callId,voiceMode);
    }

    @Override
    public boolean inviteAgent(String ip, String appId, String conversationId, String enqueue, Integer voiceMode) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(enqueue)){
            throw new RequestIllegalArgumentException();
        }
        EnQueue enQueue = null;
        try {
            Document document = DocumentHelper.parseText(enqueue);
            if(ivrActionService.validateXMLSchema(document)){
                enQueue = EnQueueDecoder.decode(enqueue);
            }
        } catch (DocumentException e) {
            throw new RequestIllegalArgumentException();
        }
        if(enQueue == null){
            throw new RequestIllegalArgumentException();
        }
        enQueue.setVoice_mode(voiceMode);

        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }
        BusinessState conversation_state = businessStateService.get(conversationId);
        if(conversation_state == null){
            throw new ConversationNotExistException();
        }
        if(conversation_state.getResId() == null){
            throw new SystemBusyException();
        }
        if(conversation_state.getClosed()!= null && conversation_state.getClosed()){
            throw new ConversationNotExistException();
        }
        if(!enQueue.getChannel().equals(conversation_state.getBusinessData().get(CallCenterUtil.CHANNEL_ID_FIELD))){
            throw new RequestIllegalArgumentException();
        }

        /**排队都是在呼叫上排队，这里是在交谈上排队，所以创建一个虚拟的呼叫call，兼容排队的逻辑**/
        String callId = UUIDGenerator.uuid();
        BusinessState state = new BusinessState.Builder()
                .setTenantId(conversation_state.getTenantId())
                .setAppId(app.getId())
                .setId(callId)
                .setResId(null)
                .setType(BusinessState.TYPE_CC_CONVERSATION_SHADOW_CALL)
                .setCallBackUrl(app.getUrl())
                .setAreaId(conversation_state.getAreaId())
                .setLineGatewayId(conversation_state.getLineGatewayId())
                .setBusinessData(new MapBuilder<String,String>()
                        .put(BusinessState.REF_RES_ID,conversation_state.getBusinessData().get(BusinessState.REF_RES_ID))
                        .putIfNotEmpty("from",conversation_state.getBusinessData().get(CallCenterUtil.CONVERSATION_SYSNUM_FIELD))
                        .putIfNotEmpty(CallCenterUtil.CALLCENTER_FIELD,conversation_state.getBusinessData().get(CallCenterUtil.CALLCENTER_FIELD))
                        .putIfNotEmpty(CallCenterUtil.ENQUEUE_START_TIME_FIELD,""+new Date().getTime())
                        .putIfNotEmpty(CallCenterUtil.CHANNEL_ID_FIELD,enQueue.getChannel())
                        .putIfNotEmpty(CallCenterUtil.CONDITION_ID_FIELD,enQueue.getRoute().getCondition().getId())
                        .putIfNotEmpty("user_data",enQueue.getData())
                        .build())
                .build();
        businessStateService.save(state);
        //开始排队
        try {
            enQueueService.lookupAgent(conversation_state.getTenantId(), conversation_state.getAppId(),
                    state.getBusinessData().get("from"),
                    callId, enQueue,CallCenterUtil.QUEUE_TYPE_INVITE_AGENT,conversationId);
        }catch (Throwable t){
            logger.error("调用呼叫中心排队失败",t);
            deQueueService.fail(conversation_state.getTenantId(),
                    conversation_state.getAppId(),callId,null,CallCenterUtil.QUEUE_TYPE_INVITE_AGENT,"调用呼叫中心排队失败");
            businessStateService.delete(callId);
            throw t;
        }
        return true;
    }

    @Override
    public String inviteOut(String ip, String appId, String conversationId, String from,
                             String to, Integer maxDial, Integer maxDuration, Integer voiceMode) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }

        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }
        BusinessState conversation_state = businessStateService.get(conversationId);
        if(conversation_state == null){
            throw new ConversationNotExistException();
        }
        if(conversation_state.getResId() == null){
            throw new SystemBusyException();
        }
        if(conversation_state.getClosed()!= null && conversation_state.getClosed()){
            throw new ConversationNotExistException();
        }
        return conversationService.inviteOut(appId,
                    conversation_state.getBusinessData().get(BusinessState.REF_RES_ID),
                    conversationId,from,to,maxDuration,maxDial,null,voiceMode);
    }
}
