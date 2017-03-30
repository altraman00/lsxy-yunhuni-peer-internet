package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.batch.CallCenterBatchInserter;
import com.lsxy.area.server.service.callcenter.*;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.CallLock;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.api.states.lock.AgentLock;
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;

    @Autowired
    private CallCenterBatchInserter callCenterBatchInserter;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Autowired
    private CallbackUrlUtil callbackUrlUtil;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Override
    public void reject(String subaccountId, String ip, String appId, String name, String queueId, String userData) throws YunhuniApiException {
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
        }
        if(StringUtils.isBlank(queueId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("queueId",queueId)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }

        CallCenterQueue callCenterQueue = callCenterQueueService.findById(queueId);
        if(callCenterQueue == null){
            throw new QueueTaskNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("queueId",queueId)
            );
        }
        String agentCallId = callCenterQueue.getAgentCallId();

        if(agentCallId == null){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("queueId",queueId)
                            .put("agentCallId",agentCallId)
            );
        }
        BusinessState state = businessStateService.get(agentCallId);

        if(state == null || (state.getClosed()!=null && state.getClosed())){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("queueId",queueId)
                            .put("agentCallId",agentCallId)
                            .put("state",state)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,state.getSubaccountId())){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("queueId",queueId)
                            .put("agentCallId",agentCallId)
                            .put("state",state)
            );
        }
        if(state.getResId() == null){
            throw new SystemBusyException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("queueId",queueId)
                            .put("agentCallId",agentCallId)
                            .put("state",state)
            );
        }

        if(state.getBusinessData().get(BusinessState.RINGING_TAG) == null){
            //不是正在振铃
            throw new SystemBusyException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("queueId",queueId)
                            .put("agentCallId",agentCallId)
                            .put("state",state)
            );
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
    public String callOut(String subaccountId,String ip, String appId, String name,
                           String from, String to, Integer maxDialSeconds, Integer maxAnswerSeconds,String userData) throws YunhuniApiException {
        if(StringUtil.isBlank(to)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("to",to)
            );
        }
        boolean isRedNum = apiGwRedBlankNumService.isRedNum(to);
        if(isRedNum){
            throw new NumberNotAllowToCallException(
                    new ExceptionContext()
                            .put("to",to)
                            .put("isRedNum",isRedNum)
            );
        }
        if(maxAnswerSeconds == null || maxAnswerSeconds > ConversationService.MAX_DURATION){
            maxAnswerSeconds = ConversationService.MAX_DURATION;
        }

        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("to",to)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("agentName",name)
                                .put("to",to)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        //判断余额配额是否充足
        calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,ProductCode.call_center.getApiCmd(), app.getTenant().getId());

        String conversationId = UUIDGenerator.uuid();
        //根据坐席name 找到坐席
        String agent = callCenterAgentService.getId(appId,subaccountId,name);
        if(StringUtil.isEmpty(agent)){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
        }
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agent);
        if(callCenterAgent == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,callCenterAgent.getSubaccountId())){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
            );
        }
        //获取坐席状态
        AgentState.Model aState = agentState.get(agent);
        if(aState == null || aState.getState() == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        if(aState.getExtension() == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        //座席没有报道
        if (aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()) {
            throw new AgentExpiredException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        ExtensionState.Model eState = extensionState.get(aState.getExtension());

        //分机不可用
        if(eState == null || !ExtensionState.Model.ENABLE_TRUE.equals(eState.getEnable())){
            throw new ExtensionUnEnableException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
                            .put("extensionstate",eState)
            );
        }
        AppExtension extension = appExtensionService.findById(aState.getExtension());
        if(extension == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
                            .put("extensionstate",eState)
            );
        }
        BusinessState state = null;
        String callId = agentIdCallReference.get(agent);
        if(callId != null){
            state = businessStateService.get(callId);
        }
        //有正在处理的交谈
        if(state != null && (state.getClosed() == null || !state.getClosed())){
            if(state.getBusinessData().get(CallCenterUtil.INVITETO_FIELD) != null){
                throw new SystemBusyException("已经在邀请呼叫了to="+state.getBusinessData().get(CallCenterUtil.INVITETO_FIELD));
            }
            if(state.getBusinessData().get(IVRActionService.IVR_ANSWER_WAITTING_FIELD) != null){
                throw new SystemBusyException("呼叫正在等待应答callid="+callId);
            }

            if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType())){
                CallLock lock = new CallLock(redisCacheService,callId);
                if(!lock.lock()){
                    throw new SystemBusyException("加锁失败callid="+callId);
                }
                try{
                    //分机短号
                    String from_extensionnum = state.getBusinessData().get(CallCenterUtil.DIRECT_HOT_FIELD);
                    //分机前缀
                    String extension_prefix = state.getBusinessData().get(CallCenterUtil.DIRECT_EXTENSIONPREFIX_FIELD);
                    if(from_extensionnum == null){
                        throw new IllegalArgumentException();
                    }
                    if(state.getBusinessData().get(CallCenterUtil.DIRECT_RECEIVE_ING_FIELD)!=null){
                        Map<String, Object> stop_params = new MapBuilder<String,Object>()
                                .putIfNotEmpty("res_id",state.getResId())
                                .putIfNotEmpty("user_data",state.getId())
                                .put("areaId",state.getAreaId())
                                .build();
                        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_RECEIVE_DTMF_STOP, stop_params);
                        if(!businessStateService.closed(callId)) {
                            try {
                                rpcCaller.invoke(sessionContext, rpcrequest,true);
                            } catch (Throwable t) {
                                throw new InvokeCallException(t);
                            }
                        }
                    }
                    businessStateService.deleteInnerField(callId,CallCenterUtil.DIRECT_HOT_FIELD,CallCenterUtil.DIRECT_EXTENSIONPREFIX_FIELD);
                }finally {
                    lock.unlock();
                }
            }
            //TODO 将其他交谈全部设置为保持（cti需要提供批量） 这里应该是阻塞调用好点
            //TODO 暂不支持呼叫同时在多个交谈先throw exception
            else{
                throw new AgentIsBusyException(new ExceptionContext().put("subaccountId",subaccountId)
                        .put("appId",appId)
                        .put("agentName",name)
                        .put("agentId",agent)
                        .put("agentstate",aState)
                        .put("extensionstate",eState));
            }
            //创建新的交谈，交谈创建成功事件中将坐席加入到新的交谈， 坐席加入交谈成功事件中呼叫外线，在振铃事件中把外线加入交谈 交谈正式开始
            conversationService.create(subaccountId,conversationId,
                    CallCenterUtil.CONVERSATION_TYPE_CALL_OUT,
                    state.getBusinessData().get(BusinessState.REF_RES_ID),state,
                    state.getTenantId(),state.getAppId(),state.getAreaId(),state.getCallBackUrl(),maxAnswerSeconds,null,userData);
            //坐席加入交谈成功事件中要呼叫这个号码
            businessStateService.updateInnerField(callId,CallCenterUtil.INVITEFROM_FIELD,from!=null?from:"",CallCenterUtil.INVITETO_FIELD,to);
        }else{
            AgentLock agentLock = new AgentLock(redisCacheService,agent);
            if(!agentLock.lock()){
                throw new SystemBusyException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("agentName",name)
                                .put("agentId",agent)
                );
            }
            //坐席加锁
            //呼叫坐席接通后,创建新的交谈,交谈创建成功事件中将坐席加入到交谈， 坐席加入交谈成功事件中呼叫外线，在振铃事件中把外线加入交谈 交谈正式开始
            //FINALLY 坐席解锁
            try {
                if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(agent))){
                    throw new AgentIsBusyException(
                            new ExceptionContext().put("subaccountId",subaccountId)
                                    .put("appId",appId)
                                    .put("agentName",name)
                                    .put("agentId",agent)
                    );
                }
                try{
                    callId = conversationService.agentCall(subaccountId,appId,null,conversationId,agent,
                            callCenterAgent.getName(),
                            extension.getId(),from,extension.getTelnum(),extension.getType(),extension.getUser(),maxAnswerSeconds,maxDialSeconds,null,userData);
                    agentState.setState(agent,CallCenterAgent.STATE_FETCHING);
                    //坐席加入交谈成功事件中要呼叫这个号码
                    businessStateService.updateInnerField(callId,CallCenterUtil.INVITEFROM_FIELD,(from == null?"":from),CallCenterUtil.INVITETO_FIELD,to);
                    CallCenter callCenter = new CallCenter();
                    callCenter.setId(callId);
                    callCenter.setTenantId(app.getTenant().getId());
                    callCenter.setAppId(app.getId());
                    callCenter.setFromNum(from);
                    callCenter.setToNum(to);
                    callCenter.setStartTime(new Date());
                    callCenter.setType(""+CallCenter.CALL_UP);
                    callCenter.setCost(BigDecimal.ZERO);
                    callCenter.setAgent(name);
                    callCenterBatchInserter.put(callCenter);
                    try{
                        callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(app.getTenant().getId(),app.getId(),
                                new Date()).setCallOut(1L).build());
                    }catch (Throwable t){
                        logger.error(String.format("incrIntoRedis失败,appId=%s",state.getAppId()),t);
                    }
                    callCenterUtil.agentStateChangedEvent(subaccountId,callbackUrlUtil.get(app,subaccountId),agent,name,
                            CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING,userData);
                }catch (Throwable t){
                    agentState.setState(agent,CallCenterAgent.STATE_IDLE);
                    throw t;
                }
            }finally {
                agentLock.unlock();
            }
        }
        return callId;
    }

    @Override
    public String callAgent(String subaccountId,String ip, String appId, String name, String from, String enqueueXml, Integer maxDialSeconds, Integer maxAnswerSeconds) throws YunhuniApiException {
        if(StringUtil.isBlank(enqueueXml)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("enqueueXml",enqueueXml)
            );
        }
        try {
            if(!ivrActionService.validateXMLSchemaIgnoreResponse(enqueueXml)){
                throw new RequestIllegalArgumentException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("enqueueXml",enqueueXml)
                );
            }
        } catch (DocumentException e) {
            logger.info("解析xml出错",e);
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("enqueueXml",enqueueXml)
            );
        }

        EnQueue enQueue = null;

        try{
            enQueue = EnQueueDecoder.decode(enqueueXml);
        }catch (Throwable t){
            throw new RequestIllegalArgumentException();
        }

        if(maxAnswerSeconds == null || maxAnswerSeconds > ConversationService.MAX_DURATION){
            maxAnswerSeconds = ConversationService.MAX_DURATION;
        }

        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        //判断余额配额是否充足
        calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,ProductCode.call_center.getApiCmd(), app.getTenant().getId());

        String conversationId = UUIDGenerator.uuid();
        //根据坐席name 找到坐席
        String agent = callCenterAgentService.getId(appId,subaccountId,name);
        if(StringUtil.isEmpty(agent)){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
        }
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agent);
        if(callCenterAgent == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,callCenterAgent.getSubaccountId())){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agent",callCenterAgent)
            );
        }
        //获取坐席状态
        AgentState.Model aState = agentState.get(agent);
        if(aState == null || aState.getState() == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        if(aState.getExtension() == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
            );
        }
        //座席没有报道
        if (aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()) {
            throw new AgentExpiredException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        ExtensionState.Model eState = extensionState.get(aState.getExtension());

        //分机不可用
        if(eState == null || !ExtensionState.Model.ENABLE_TRUE.equals(eState.getEnable())){
            throw new ExtensionUnEnableException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("extension_id",aState.getExtension())
                            .put("extensionState",eState)
            );
        }
        AppExtension extension = appExtensionService.findById(aState.getExtension());
        if(extension == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("extension_id",aState.getExtension())
            );
        }
        BusinessState state = null;
        String callId = agentIdCallReference.get(agent);
        if(callId != null){
            state = businessStateService.get(callId);
        }
        //有正在处理的交谈
        if(state != null && (state.getClosed() == null || !state.getClosed())){
            if(state.getBusinessData().get(CallCenterUtil.ENQUEUEXML_FIELD) != null){
                throw new SystemBusyException();
            }
            //TODO 将其他交谈全部设置为保持（cti需要提供批量） 这里应该是阻塞调用好点
            throw new AgentIsBusyException(new ExceptionContext().put("subaccountId",subaccountId)
                    .put("appId",appId)
                    .put("agentName",name)
                    .put("agentId",agent)
                    .put("agentstate",aState)
                    .put("extensionstate",eState));

            /*//创建新的交谈，交谈创建成功事件中将坐席加入到新的交谈， 坐席加入交谈成功事件中进行排队，在振铃事件中把排到的坐席加入交谈 交谈正式开始
            conversationService.create(subaccountId,conversationId,
                    CallCenterUtil.CONVERSATION_TYPE_CALL_AGENT,
                    state.getBusinessData().get(BusinessState.REF_RES_ID),state,
                    state.getTenantId(),state.getAppId(),state.getAreaId(),state.getCallBackUrl(),maxAnswerSeconds,null,enQueue.getUser_data());
            //坐席加入交谈成功事件中要排队找坐席
            businessStateService.updateInnerField(callId,CallCenterUtil.ENQUEUEXML_FIELD,enqueueXml);*/
        }else{
            AgentLock agentLock = new AgentLock(redisCacheService,agent);
            if(!agentLock.lock()){
                throw new SystemBusyException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("agentName",name)
                                .put("agentId",agent)
                                .put("extension_id",aState.getExtension())
                );
            }
            //坐席加锁
            //呼叫坐席接通后,创建新的交谈,交谈创建成功事件中将坐席加入到交谈， 坐席加入交谈成功事件中进行排队，在振铃事件中把排到的坐席加入交谈 交谈正式开始
            //FINALLY 坐席解锁
            try {
                if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(agent))){
                    throw new AgentIsBusyException(
                            new ExceptionContext().put("subaccountId",subaccountId)
                                    .put("appId",appId)
                                    .put("agentName",name)
                                    .put("agentId",agent)
                                    .put("extension_id",aState.getExtension())
                    );
                }
                try{
                    callId = conversationService.agentCall(subaccountId,appId,null,conversationId,agent,
                            callCenterAgent.getName(),
                            extension.getId(),null,extension.getTelnum(),extension.getType(),extension.getUser(),maxAnswerSeconds,maxDialSeconds,null,enQueue.getUser_data());
                    agentState.setState(agent,CallCenterAgent.STATE_FETCHING);
                    //坐席加入交谈成功事件中要排队找坐席
                    businessStateService.updateInnerField(callId,CallCenterUtil.ENQUEUEXML_FIELD,enqueueXml);
                    CallCenter callCenter = new CallCenter();
                    callCenter.setId(callId);
                    callCenter.setTenantId(app.getTenant().getId());
                    callCenter.setAppId(app.getId());
                    callCenter.setFromNum(from);
                    callCenter.setToNum(name);
                    callCenter.setStartTime(new Date());
                    callCenter.setType(""+CallCenter.CALL_UP);
                    callCenter.setCost(BigDecimal.ZERO);
                    callCenterBatchInserter.put(callCenter);
                    try{
                        callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(app.getTenant().getId(),app.getId(),
                                new Date()).setCallOut(1L).build());
                    }catch (Throwable t){
                        logger.error("incrIntoRedis失败",t);
                    }
                    callCenterUtil.agentStateChangedEvent(subaccountId,callbackUrlUtil.get(app,subaccountId),agent,name,
                            CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING,enQueue.getUser_data());
                }catch (Throwable t){
                    agentState.setState(agent,CallCenterAgent.STATE_IDLE);
                    throw t;
                }
            }finally {
                agentLock.unlock();
            }
        }
        return callId;
    }


    @Override
    public boolean setVoiceMode(String subaccountId, String appId, String ip, String name, String conversationId, Integer mode) throws YunhuniApiException {
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
        }
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("conversation_id",conversationId)
            );
        }
        if(mode == null){
            mode = CallCenterConversationMember.MODE_DEFAULT;
        }
        if(!ArrayUtils.contains(CallCenterConversationMember.MODE_ARRAY,mode)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("conversationId",conversationId)
                            .put("voice",mode)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String agentId = callCenterAgentService.getId(appId,subaccountId,name);
        if(agentId == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
        }
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
                            .put("agentId",agentId)
            );
        }
        return conversationService.setVoiceMode(conversationId,callId,mode);
    }

    @Override
    public boolean enter(String subaccountId, String appId, String ip, String name, String conversationId, Integer mode, Boolean holding) throws YunhuniApiException {

        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
            );
        }
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
            );
        }
        if(mode == null){
            mode = CallCenterConversationMember.MODE_DEFAULT;
        }
        if(!ArrayUtils.contains(CallCenterConversationMember.MODE_ARRAY,mode)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("voice_mode",mode)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        BusinessState conversationState = businessStateService.get(conversationId);

        if(conversationState == null || (conversationState.getClosed()!= null && conversationState.getClosed())){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
            );
        }

        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,conversationState.getSubaccountId())){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
            );
        }
        if(conversationState.getResId() == null){
            throw new SystemBusyException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
            );
        }
        //判断余额配额是否充足
        calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,ProductCode.call_center.getApiCmd(), app.getTenant().getId());

        //根据坐席name 找到坐席
        String agent = callCenterAgentService.getId(appId,subaccountId,name);
        if(StringUtil.isEmpty(agent)){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
            );
        }
        CallCenterAgent callCenterAgent = callCenterAgentService.findById(agent);
        if(callCenterAgent == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,callCenterAgent.getSubaccountId())){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
            );
        }
        //获取坐席状态
        AgentState.Model aState = agentState.get(agent);
        if(aState == null || aState.getState() == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        if(aState.getExtension() == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        //座席没有报道
        if (aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()) {
            throw new AgentExpiredException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
            );
        }
        ExtensionState.Model eState = extensionState.get(aState.getExtension());

        //分机不可用
        if(eState == null || !ExtensionState.Model.ENABLE_TRUE.equals(eState.getEnable())){
            throw new ExtensionUnEnableException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
                            .put("extensionstate",eState)
            );
        }
        AppExtension extension = appExtensionService.findById(aState.getExtension());
        if(extension == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agent)
                            .put("agentstate",aState)
                            .put("extensionstate",eState)
            );
        }
        BusinessState state = null;
        String callId = agentIdCallReference.get(agent);
        if(callId != null){
            state = businessStateService.get(callId);
        }

        if(state != null && (state.getClosed() == null || !state.getClosed())){
            //呼叫已经存在
            String curConversation = callConversationService.head(callId);//原有交谈

            //TODO 将其他交谈全部设置为保持（cti需要提供批量） 这里应该是阻塞调用好点 先throw exception
            throw new AgentIsBusyException(new ExceptionContext().put("subaccountId",subaccountId)
                    .put("appId",appId)
                    .put("agentName",name)
                    .put("agentId",agent)
                    .put("agentstate",aState)
                    .put("extensionstate",eState));
            /*try {
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
                logger.info("加入交谈失败",e);
                //--是否需要调用退出
                conversationService.logicExit(conversationId,callId);
                throw e;
            }*/
        }else{
            //呼叫不存在 需要呼叫坐席
            AgentLock agentLock = new AgentLock(redisCacheService,agent);
            if(!agentLock.lock()){
                throw new SystemBusyException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("conversation_id",conversationId)
                                .put("agentName",name)
                                .put("agentId",agent)
                );
            }
            //坐席加锁
            //呼叫坐席接通后,将坐席加入到交谈
            //FINALLY 坐席解锁
            try {
                if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(agent))){
                    throw new AgentIsBusyException(
                            new ExceptionContext().put("subaccountId",subaccountId)
                                    .put("appId",appId)
                                    .put("conversation_id",conversationId)
                                    .put("agentName",name)
                                    .put("agentId",agent)
                    );
                }
                try{
                    callId = conversationService.agentCall(subaccountId,appId,conversationState.getBusinessData().get(BusinessState.REF_RES_ID),conversationId,agent,
                            callCenterAgent.getName(),
                            extension.getId(),null,extension.getTelnum(),extension.getType(),extension.getUser(),null,null,null,conversationState.getUserdata());
                    agentState.setState(agent,CallCenterAgent.STATE_FETCHING);
                    callCenterUtil.agentStateChangedEvent(subaccountId,callbackUrlUtil.get(app,subaccountId),agent,name,
                            CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING,conversationState.getUserdata());
                }catch (Throwable t){
                    agentState.setState(agent,CallCenterAgent.STATE_IDLE);
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
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
            );
        }
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("conversation_id",conversationId)
                                .put("agentName",name)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }

        String agentId = callCenterAgentService.getId(appId,subaccountId,name);
        if(agentId == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
            );
        }
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agentId)
                            .put("callId",callId)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,businessStateService.subaccountId(callId))){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agentId)
                            .put("callId",callId)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,businessStateService.subaccountId(conversationId))){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("agentName",name)
                            .put("agentId",agentId)
                            .put("callId",callId)
            );
        }
        conversationService.exit(conversationId,callId);
        return true;
    }

    @Override
    public List<CallCenterConversationDetail> conversations(String subaccountId, String appId, String ip, String name) throws YunhuniApiException{
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("agentName",name)
                                .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }

        String agentId = callCenterAgentService.getId(appId,subaccountId,name);
        if(agentId == null){
            throw new AgentNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("agentName",name)
            );
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
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,state.getSubaccountId())){
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
                    detail.setSubaccountId(conversation.getSubaccountId());
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
