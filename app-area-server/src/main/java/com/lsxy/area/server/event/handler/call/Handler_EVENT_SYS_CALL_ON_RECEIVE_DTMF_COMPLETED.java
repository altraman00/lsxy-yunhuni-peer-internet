package com.lsxy.area.server.event.handler.call;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.CallLock;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.SipUrlUtil;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
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
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
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
public class Handler_EVENT_SYS_CALL_ON_RECEIVE_DTMF_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_RECEIVE_DTMF_COMPLETED.class);

    @Autowired
    private AppService appService;


    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Reference(timeout=3000,check = false,lazy = true)
    private AppExtensionService appExtensionService;

    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;
    
    @Autowired
    private ExtensionState extensionState;

    @Autowired
    private AgentState agentState;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_RECEIVE_DTMF_COMPLETED;
    }

    /**
     * 处理收码结束事件
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
            throw new InvalidParamException("businessstate is null,call_id={}",call_id);
        }

        if(logger.isDebugEnabled()){
            logger.debug("call_id={},state={}",call_id,state);
        }

        String keys = (String)params.get("keys");
        String error = (String)params.get("error");

        if(StringUtil.isNotBlank(error)){
            logger.info("收码失败id={},error={}",call_id,error);
        }

        if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType())){
            //热线收码结束标记
            businessStateService.deleteInnerField(CallCenterUtil.DIRECT_RECEIVE_ING_FIELD);
            //分机短号
            String from_extensionnum = state.getBusinessData().get(CallCenterUtil.DIRECT_HOT_FIELD);
            //分机前缀
            String extension_prefix = state.getBusinessData().get(CallCenterUtil.DIRECT_EXTENSIONPREFIX_FIELD);
            String to = keys;
            String conversationId = UUIDGenerator.uuid();
            if(from_extensionnum == null){
                throw new IllegalArgumentException();
            }
            CallLock lock = new CallLock(redisCacheService,call_id);
            if(!lock.lock()){
                logger.info("呼叫加锁失败callid={}",call_id);
                return res;
            }
            try{
                businessStateService.deleteInnerField(CallCenterUtil.DIRECT_HOT_FIELD,CallCenterUtil.DIRECT_EXTENSIONPREFIX_FIELD);
                if(StringUtil.isNotBlank(error) || StringUtils.isBlank(to)){
                    throw new IllegalArgumentException(String.format("收码失败callid=%s,error=%s,keys=%s",call_id,error,keys));
                }
                //判断是呼给外线 还是 其他分机
                if(SipUrlUtil.isHotNum(to)){//不允许呼给热线
                    throw new NumberNotAllowToCallException(new ExceptionContext()
                            .put("number",to)
                            .put("callid",call_id)
                    );
                }
                if(SipUrlUtil.isShortNum(extension_prefix,to)){//被叫是分机短号
                    //流程：会议创建成功后将call加入会议，加入会议成功事件 呼叫被叫，振铃事件将被叫加入会议
                    AgentLock to_agentLock = null;
                    try{
                        //判断被叫分机是否存在
                        String to_extensionnum = extension_prefix + to;//被叫号码要为长号码
                        if(logger.isDebugEnabled()){
                            logger.info("直拨分机，to={}",to_extensionnum);
                        }
                        //不能自己呼给自己
                        if(to_extensionnum.equals(extension_prefix+from_extensionnum)){
                            throw new NumberNotAllowToCallException(
                                    new ExceptionContext()
                                    .put("from",extension_prefix+from_extensionnum)
                                    .put("to",to_extensionnum)
                            );
                        }
                        //判断主叫分机是否存在，不合法直接拒绝
                        AppExtension to_appExtension = appExtensionService.getByUser(to_extensionnum);
                        if(to_appExtension == null){
                            throw new ExtensionNotExistException(
                                    new ExceptionContext()
                                    .put("extension_user",to_extensionnum)
                            );
                        }
                        //根据分机找到坐席，找不到坐席直接拒绝
                        ExtensionState.Model to_eState = extensionState.get(to_appExtension.getId());
                        if(to_eState == null){
                            throw new ExtensionNotExistException(
                                    new ExceptionContext()
                                            .put("extension_id",to_appExtension.getId())
                            );
                        }
                        if(!to_eState.getEnable().equals(ExtensionState.Model.ENABLE_TRUE)){
                            throw new ExtensionUnEnableException(
                                    new ExceptionContext()
                                            .put("extension_state",to_eState)
                            );
                        }
                        String to_agentId = to_eState.getAgent();
                        if(StringUtil.isBlank(to_agentId)){
                            throw new AgentNotExistException(
                                    new ExceptionContext()
                                            .put("extension_state",to_eState)
                            );
                        }
                        CallCenterAgent to_agent = callCenterAgentService.findById(to_agentId);
                        if(to_agent == null){
                            throw new AgentNotExistException(
                                    new ExceptionContext()
                                            .put("agentId",to_agentId)
                            );
                        }
                        AgentState.Model to_aState = agentState.get(to_agentId);
                        if(to_aState == null){
                            throw new AgentNotExistException(
                                    new ExceptionContext()
                                            .put("agentId",to_agentId)
                            );
                        }
                        if(to_aState.getLastRegTime() + AgentState.REG_EXPIRE < System.currentTimeMillis()){
                            throw new AgentExpiredException(
                                    new ExceptionContext()
                                            .put("agentstate",to_aState)
                            );
                        }
                        //坐席加锁，加锁失败直接拒绝
                        to_agentLock = new AgentLock(redisCacheService,to_agentId);
                        if(!to_agentLock.lock()){
                            throw new SystemBusyException(
                                    new ExceptionContext()
                                            .put("agentid",to_agentId)
                            );
                        }
                        //判断坐席状态是否是空闲，非空闲直接拒绝
                        if(!CallCenterAgent.STATE_IDLE.equals(agentState.getState(to_agentId))){
                            throw new SystemBusyException(
                                    new ExceptionContext()
                                            .put("agentid",to_agentId)
                                            .put("state",agentState.getState(to_agentId))
                            );
                        }
                        conversationService.create(state.getSubaccountId(),conversationId, CallCenterUtil.CONVERSATION_TYPE_CALL_AGENT,
                                state.getBusinessData().get(BusinessState.REF_RES_ID),state,state.getTenantId(),state.getAppId(),
                                state.getAreaId(),state.getCallBackUrl(), ConversationService.MAX_DURATION,null,state.getUserdata());
                        businessStateService.updateInnerField(
                                call_id,
                                //直拨被叫-坐席分机
                                CallCenterUtil.DIRECT_AGENT_FIELD,to_agentId,
                                //直拨主叫
                                CallCenterUtil.DIRECT_FROM_FIELD,from_extensionnum
                        );
                    }catch (Throwable t){
                        logger.info("",t);
                    }finally {
                        //finally 坐席解锁
                        if(to_agentLock!=null){
                            try{
                                to_agentLock.unlock();
                            }catch (Throwable t){
                                logger.info("",t);
                            }
                        }
                    }
                }else if(SipUrlUtil.isOut(to)) {//被叫是外线
                    if(logger.isDebugEnabled()){
                        logger.info("直拨外线，to={}",to);
                    }
                    boolean isRedNum = apiGwRedBlankNumService.isRedNum(to);
                    if(isRedNum){
                        throw new NumberNotAllowToCallException(
                                new ExceptionContext()
                                        .put("to",to)
                                        .put("isRedNum",isRedNum)
                        );
                    }
                    conversationService.create(state.getSubaccountId(),conversationId, CallCenterUtil.CONVERSATION_TYPE_CALL_OUT,
                            state.getBusinessData().get(BusinessState.REF_RES_ID),state,state.getTenantId(),state.getAppId(),
                            state.getAreaId(),state.getCallBackUrl(), ConversationService.MAX_DURATION,null,state.getUserdata());
                    businessStateService.updateInnerField(call_id,
                            //直拨被叫-外线
                            CallCenterUtil.DIRECT_OUT_FIELD,to
                    );
                }else{
                    throw new NumberNotAllowToCallException(new ExceptionContext()
                            .put("number",to)
                            .put("callid",call_id)
                    );
                }
            }catch (Throwable t){
                hangup(state.getResId(),call_id,state.getAreaId());
            }finally {
                lock.unlock();
            }
        }else if(canDoivr(state)){
            Long begin_time = null;
            Long end_time = null;
            if(params.get("begin_time") != null){
                begin_time = (Long.parseLong(params.get("begin_time").toString())) * 1000;
            }
            if(params.get("end_time") != null){
                end_time = (Long.parseLong(params.get("end_time").toString())) * 1000;
            }
            if(StringUtils.isNotBlank(state.getCallBackUrl())){
                Map<String, Object> notify_data = new MapBuilder<String, Object>()
                        .putIfNotEmpty("event", "ivr.get_end")
                        .putIfNotEmpty("id", call_id)
                        .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                        .putIfNotEmpty("begin_time", begin_time)
                        .putIfNotEmpty("end_time", end_time)
                        .putIfNotEmpty("error", params.get("error"))
                        .putIfNotEmpty("keys", params.get("keys"))
                        .build();
                notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,null,3);
            }
            ivrActionService.doAction(call_id,new MapBuilder<String,Object>()
                    .putIfNotEmpty("keys",keys)
                    .putIfNotEmpty("error",error)
                    .build());
        }
        return res;
    }

    private boolean canDoivr(BusinessState state){
        if(BusinessState.TYPE_IVR_CALL.equals(state.getType())){//是ivr呼出
            return true;
        }
        if(BusinessState.TYPE_IVR_INCOMING.equals(state.getType())){//是ivr呼入
            return true;
        }
        return false;
    }

    private void hangup(String res_id,String call_id,String area_id){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("user_data",call_id)
                .put("areaId",area_id)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            if(!businessStateService.closed(call_id)) {
                rpcCaller.invoke(sessionContext, rpcrequest, true);
            }
        } catch (Throwable e) {
            logger.error(String.format("调用挂断失败,callid=%s",call_id),e);
        }
    }
}
