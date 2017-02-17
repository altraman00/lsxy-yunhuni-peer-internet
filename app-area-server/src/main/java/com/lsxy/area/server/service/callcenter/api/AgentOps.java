package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.callcenter.*;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.api.states.lock.AgentLock;
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
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
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by liuws on 2017/1/9.
 */
@Service
@Component
public class AgentOps implements com.lsxy.call.center.api.service.AgentOps {

    public static final Logger logger = LoggerFactory.getLogger(AgentOps.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private CallConversationService callConversationService;

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
    private PlayFileUtil playFileUtil;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterQueueService callCenterQueueService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterAgentService callCenterAgentService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private AppExtensionService appExtensionService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private AgentState agentState;

    @Autowired
    private ExtensionState extensionState;

    @Override
    public void reject(String subaccountId, String ip, String appId, String name, String queueId, String userData) throws YunhuniApiException {
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(queueId)){
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

        CallCenterQueue callCenterQueue = callCenterQueueService.findById(queueId);
        if(callCenterQueue == null){
            logger.warn("找不到对应的排队记录id={}",queueId);
            throw new RequestIllegalArgumentException();
        }
        String agentCallId = callCenterQueue.getAgentCallId();

        BusinessState state = businessStateService.get(agentCallId);

        if(state == null || (state.getClosed()!=null && state.getClosed())){
            throw new CallNotExistsException();
        }

        if(state.getResId() == null){
            throw new SystemBusyException();
        }

        if(state.getBusinessData().get(BusinessState.RINGING_TAG) == null){
            //不是正在振铃
            throw new SystemBusyException();
        }

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("user_data",state.getId())
                .put("areaId",state.getAreaId())
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            throw new InvokeCallException();
        }
    }

    @Override
    public boolean callOut(String subaccountId,String ip, String appId, String name, String from, String to, Integer maxDialSeconds, Integer maxAnswerSeconds) throws YunhuniApiException {
        if(StringUtil.isBlank(to)){
            throw new RequestIllegalArgumentException();
        }
        if(maxAnswerSeconds == null || maxAnswerSeconds > ConversationService.MAX_DURATION){
            maxAnswerSeconds = ConversationService.MAX_DURATION;
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
        String conversationId = UUIDGenerator.uuid();
        //根据坐席name 找到坐席
        String agent = callCenterAgentService.getId(appId,name);
        if(StringUtil.isEmpty(agent)){
            throw new AgentNotExistException();
        }
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agent);
        if(callCenterAgent == null){
            throw new AgentNotExistException();
        }
        //获取坐席状态
        AgentState.Model aState = agentState.get(agent);
        if(aState == null || aState.getState() == null){
            throw new AgentNotExistException();
        }
        if(aState.getExtension() == null){
            throw new ExtensionNotExistException();
        }
        //座席没有报道
        if (aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()) {
            throw new AgentExpiredException();
        }
        ExtensionState.Model eState = extensionState.get(aState.getExtension());

        //分机不可用
        if(eState == null || !ExtensionState.Model.ENABLE_TRUE.equals(eState.getEnable())){
            throw new ExtensionUnEnableException();
        }
        AppExtension extension = appExtensionService.findById(aState.getExtension());
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        BusinessState state = businessStateService.get(agent);
        //有正在处理的交谈
        if(state != null && (state.getClosed() == null || !state.getClosed())){
            //TODO 将其他交谈全部设置为保持（cti需要提供批量） 这里应该是阻塞调用好点

            //创建新的交谈，交谈创建成功事件中将坐席加入到新的交谈， 坐席加入交谈成功事件中呼叫外线，在振铃事件中把外线加入交谈 交谈正式开始
            conversationService.create(subaccountId,conversationId,
                    state.getBusinessData().get(BusinessState.REF_RES_ID),null,state,
                    state.getTenantId(),state.getAppId(),state.getAreaId(),state.getCallBackUrl(),maxAnswerSeconds,null);
            //坐席加入交谈成功事件中要呼叫这个号码
            businessStateService.updateInnerField(conversationId,"invite_from",from,"invite_to",to);
        }else{
            AgentLock agentLock = new AgentLock(redisCacheService,agent);
            if(!agentLock.lock()){
                throw new SystemBusyException();
            }
            //坐席加锁
            //呼叫坐席接通后,创建新的交谈,交谈创建成功事件中将坐席加入到交谈， 坐席加入交谈成功事件中呼叫外线，在振铃事件中把外线加入交谈 交谈正式开始
            //FINALLY 坐席解锁
            try {
                if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(agent))){
                    throw new SystemBusyException();
                }
                try{
                    String callId = conversationService.agentCall(subaccountId,appId,conversationId,agent,
                            callCenterAgent.getName(),
                            extension.getId(),from,extension.getTelnum(),extension.getType(),extension.getUser(),maxAnswerSeconds,maxDialSeconds,null);
                    callCenterAgentService.state(app.getTenant().getId(),appId,agent,CallCenterAgent.STATE_FETCHING,true);
                    //坐席加入交谈成功事件中要呼叫这个号码
                    businessStateService.updateInnerField(callId,"invite_from",from,"invite_to",to);
                }catch (Throwable t){
                    callCenterAgentService.state(app.getTenant().getId(),appId,agent,CallCenterAgent.STATE_IDLE,true);
                    throw t;
                }
            }finally {
                agentLock.unlock();
            }
        }
        return true;
    }

    @Override
    public boolean callAgent(String subaccountId,String ip, String appId, String name, String from, String enqueueXml, Integer maxDialSeconds, Integer maxAnswerSeconds) throws YunhuniApiException {
        if(StringUtil.isBlank(enqueueXml)){
            throw new RequestIllegalArgumentException();
        }
        try {
            Document doc = DocumentHelper.parseText(enqueueXml);
            if(!ivrActionService.validateXMLSchema(doc)){
                throw new RequestIllegalArgumentException();
            }
        } catch (DocumentException e) {
            throw new RequestIllegalArgumentException();
        }

        if(maxAnswerSeconds == null || maxAnswerSeconds > ConversationService.MAX_DURATION){
            maxAnswerSeconds = ConversationService.MAX_DURATION;
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
        String conversationId = UUIDGenerator.uuid();
        //根据坐席name 找到坐席
        String agent = callCenterAgentService.getId(appId,name);
        if(StringUtil.isEmpty(agent)){
            throw new AgentNotExistException();
        }
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agent);
        if(callCenterAgent == null){
            throw new AgentNotExistException();
        }
        //获取坐席状态
        AgentState.Model aState = agentState.get(agent);
        if(aState == null || aState.getState() == null){
            throw new AgentNotExistException();
        }
        if(aState.getExtension() == null){
            throw new ExtensionNotExistException();
        }
        //座席没有报道
        if (aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()) {
            throw new AgentExpiredException();
        }
        ExtensionState.Model eState = extensionState.get(aState.getExtension());

        //分机不可用
        if(eState == null || !ExtensionState.Model.ENABLE_TRUE.equals(eState.getEnable())){
            throw new ExtensionUnEnableException();
        }
        AppExtension extension = appExtensionService.findById(aState.getExtension());
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        BusinessState state = null;
        String callId = agentIdCallReference.get(agent);
        if(callId != null){
            state = businessStateService.get(callId);
        }
        //有正在处理的交谈
        if(state != null && (state.getClosed() == null || !state.getClosed())){
            //TODO 将其他交谈全部设置为保持（cti需要提供批量） 这里应该是阻塞调用好点

            //创建新的交谈，交谈创建成功事件中将坐席加入到新的交谈， 坐席加入交谈成功事件中进行排队，在振铃事件中把排到的坐席加入交谈 交谈正式开始
            conversationService.create(subaccountId,conversationId,
                    state.getBusinessData().get(BusinessState.REF_RES_ID),null,state,
                    state.getTenantId(),state.getAppId(),state.getAreaId(),state.getCallBackUrl(),maxAnswerSeconds,null);
            //坐席加入交谈成功事件中要排队找坐席
            businessStateService.updateInnerField(conversationId,"enqueue_xml",enqueueXml);
        }else{
            AgentLock agentLock = new AgentLock(redisCacheService,agent);
            if(!agentLock.lock()){
                throw new SystemBusyException();
            }
            //坐席加锁
            //呼叫坐席接通后,创建新的交谈,交谈创建成功事件中将坐席加入到交谈， 坐席加入交谈成功事件中进行排队，在振铃事件中把排到的坐席加入交谈 交谈正式开始
            //FINALLY 坐席解锁
            try {
                if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(agent))){
                    throw new SystemBusyException();
                }
                try{
                    callId = conversationService.agentCall(subaccountId,appId,conversationId,agent,
                            callCenterAgent.getName(),
                            extension.getId(),from,extension.getTelnum(),extension.getType(),extension.getUser(),maxAnswerSeconds,maxDialSeconds,null);
                    callCenterAgentService.state(app.getTenant().getId(),appId,agent,CallCenterAgent.STATE_FETCHING,true);
                    //坐席加入交谈成功事件中要排队找坐席
                    businessStateService.updateInnerField(conversationId,"enqueue_xml",enqueueXml);
                }catch (Throwable t){
                    callCenterAgentService.state(app.getTenant().getId(),appId,agent,CallCenterAgent.STATE_IDLE,true);
                    throw t;
                }
            }finally {
                agentLock.unlock();
            }
        }
        return true;
    }


    @Override
    public boolean setVoiceMode(String subaccountId, String appId, String ip, String name, String conversationId, Integer mode) throws YunhuniApiException {
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }
        if(mode == null){
            mode = CallCenterConversationMember.MODE_DEFAULT;
        }
        if(!ArrayUtils.contains(CallCenterConversationMember.MODE_ARRAY,mode)){
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
        String agentId = callCenterAgentService.getId(appId,name);
        if(agentId == null){
            throw new AgentNotExistException();
        }
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException();
        }
        return conversationService.setVoiceMode(conversationId,callId,mode);
    }

    @Override
    public boolean enter(String subaccountId, String appId, String ip, String name, String conversationId, Integer mode, Boolean holding) throws YunhuniApiException {

        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException();
        }
        if(mode == null){
            mode = CallCenterConversationMember.MODE_DEFAULT;
        }
        if(!ArrayUtils.contains(CallCenterConversationMember.MODE_ARRAY,mode)){
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
        BusinessState conversationState = businessStateService.get(conversationId);

        if(conversationState == null || (conversationState.getClosed()!= null && conversationState.getClosed())){
            throw new ConversationNotExistException();
        }

        if(conversationState.getResId() == null){
            throw new SystemBusyException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }
        //根据坐席name 找到坐席
        String agent = callCenterAgentService.getId(appId,name);
        if(StringUtil.isEmpty(agent)){
            throw new AgentNotExistException();
        }
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agent);
        if(callCenterAgent == null){
            throw new AgentNotExistException();
        }
        //获取坐席状态
        AgentState.Model aState = agentState.get(agent);
        if(aState == null || aState.getState() == null){
            throw new AgentNotExistException();
        }
        if(aState.getExtension() == null){
            throw new ExtensionNotExistException();
        }
        //座席没有报道
        if (aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()) {
            throw new AgentExpiredException();
        }
        ExtensionState.Model eState = extensionState.get(aState.getExtension());

        //分机不可用
        if(eState == null || !ExtensionState.Model.ENABLE_TRUE.equals(eState.getEnable())){
            throw new ExtensionUnEnableException();
        }
        AppExtension extension = appExtensionService.findById(aState.getExtension());
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        BusinessState state = null;
        String callId = agentIdCallReference.get(agent);
        if(callId != null){
            state = businessStateService.get(callId);
        }

        if(state != null && (state.getClosed() == null || !state.getClosed())){
            //呼叫已经存在
            String curConversation = callConversationService.head(callId);//原有交谈

            //TODO 设置所有交谈为保持状态，这里应该是阻塞调用好点,一次只能活动在一个交谈

            try {
                //加入交谈（是否需要上一步成功后再执行加入交谈）
                conversationService.join(conversationId,callId,null,null,mode);
                if(holding!=null && !holding){
                    //退出原有交谈
                    if(!businessStateService.closed(curConversation)
                            && !businessStateService.closed(callId)){
                        conversationService.exit(curConversation,callId);
                    }
                }
            } catch (YunhuniApiException e) {
                logger.info("加入交谈失败:{}",e.getCode());
                //--是否需要调用退出
                conversationService.logicExit(conversationId,callId);
                throw e;
            }
        }else{
            //呼叫不存在 需要呼叫坐席
            AgentLock agentLock = new AgentLock(redisCacheService,agent);
            if(!agentLock.lock()){
                throw new SystemBusyException();
            }
            //坐席加锁
            //呼叫坐席接通后,将坐席加入到交谈
            //FINALLY 坐席解锁
            try {
                if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(agent))){
                    throw new SystemBusyException();
                }
                try{
                    callId = conversationService.agentCall(subaccountId,appId,conversationId,agent,
                            callCenterAgent.getName(),
                            extension.getId(),null,extension.getTelnum(),extension.getType(),extension.getUser(),null,null,null);
                    callCenterAgentService.state(app.getTenant().getId(),appId,agent,CallCenterAgent.STATE_FETCHING,true);
                    //坐席加入交谈成功事件中要排队找坐席
                }catch (Throwable t){
                    callCenterAgentService.state(app.getTenant().getId(),appId,agent,CallCenterAgent.STATE_IDLE,true);
                    throw t;
                }
            }finally {
                agentLock.unlock();
            }
        }
        return true;
    }

    @Override
    public boolean exit(String subaccountId, String appId, String ip, String name, String conversationId) throws YunhuniApiException {
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException();
        }
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

        String agentId = callCenterAgentService.getId(appId,name);
        if(agentId == null){
            throw new AgentNotExistException();
        }
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException();
        }
        conversationService.exit(conversationId,callId);
        return true;
    }

    @Override
    public List<CallCenterConversationDetail> conversations(String subaccountId, String appId, String ip, String name) throws YunhuniApiException{
        if(StringUtils.isBlank(name)){
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

        String agentId = callCenterAgentService.getId(appId,name);
        if(agentId == null){
            throw new AgentNotExistException();
        }

        List<CallCenterConversationDetail> result = null;

        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            return result;
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || (state.getClosed() !=null && state.getClosed())){
            return result;
        }

        Set<String> ids = callConversationService.getConversations(callId);
        Iterable<CallCenterConversation> list = callCenterConversationService.findAll(ids);

        if(list != null){
            result = new ArrayList<>();
            for (CallCenterConversation conversation : list) {
                if(CallCenterConversation.STATE_READY.equals(conversation.getState())){
                    CallCenterConversationDetail detail = new CallCenterConversationDetail();
                    detail.setId(conversation.getId());
                    detail.setType(conversation.getType());
                    detail.setChannelId(conversation.getChannelId());
                    detail.setQueueId(conversation.getQueueId());
                    detail.setStartTime(conversation.getStartTime());//发起时间
                    detail.setEndTime(conversation.getEndTime());//结束时间
                    detail.setEndReason(conversation.getEndReason());
                    result.add(detail);
                }
            }
        }
        return result;
    }
}
