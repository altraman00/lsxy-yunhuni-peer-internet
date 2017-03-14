package com.lsxy.area.server.event.handler.call;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.framework.core.exceptions.api.AgentNotExistException;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
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
public class Handler_EVENT_CALL_ON_ANSWER_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_CALL_ON_ANSWER_COMPLETED.class);

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
    private CallCenterStatisticsService callCenterStatisticsService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private CallbackUrlUtil callbackUrlUtil;

    @Autowired
    private AgentState agentState;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_ANSWER_COMPLETED;
    }

    /**
     * 接收应答调用成功事件后需要调用ivr询问开发者下一步干嘛
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
        String error = (String)params.get("error");
        if(StringUtils.isNotBlank(error)){
            logger.error("应答出错:{},",error);
            return res;
        }
        String call_id = (String)params.get("user_data");
        if(StringUtils.isBlank(call_id)){
            throw new InvalidParamException("call_id=null");
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null,callid="+call_id);
        }
        try{
            callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(state.getTenantId(),state.getAppId(),
                    new Date()).setCallInSuccess(1L).build());
        }catch (Throwable t){
            logger.error(String.format("incrIntoRedis失败,appId=%s",state.getAppId()),t);
        }
        if(BusinessState.TYPE_CC_AGENT_CALL.equals(state.getType())){
            //分机直拨
            String conversationId = state.getBusinessData().get(CallCenterUtil.CONVERSATION_FIELD);
            try{
                agentState.setState(state.getBusinessData().get(CallCenterUtil.AGENT_ID_FIELD),CallCenterAgent.STATE_TALKING);
                callCenterUtil.agentStateChangedEvent(state.getSubaccountId(),state.getCallBackUrl(),state.getBusinessData().get(CallCenterUtil.AGENT_ID_FIELD)
                        ,state.getBusinessData().get(CallCenterUtil.AGENT_NAME_FIELD),
                        CallCenterAgent.STATE_FETCHING,CallCenterAgent.STATE_TALKING,state.getUserdata());
                if(state.getBusinessData().get(CallCenterUtil.DIRECT_AGENT_FIELD) != null){//直拨坐席
                    conversationService.create(state.getSubaccountId(),conversationId,CallCenterUtil.CONVERSATION_TYPE_CALL_AGENT,
                            state.getBusinessData().get(BusinessState.REF_RES_ID),state,state.getTenantId(),state.getAppId(),
                            state.getAreaId(),state.getCallBackUrl(),ConversationService.MAX_DURATION,null,state.getUserdata());
                }else if(state.getBusinessData().get(CallCenterUtil.DIRECT_OUT_FIELD) != null){
                    conversationService.create(state.getSubaccountId(),conversationId,CallCenterUtil.CONVERSATION_TYPE_CALL_OUT,
                            state.getBusinessData().get(BusinessState.REF_RES_ID),state,state.getTenantId(),state.getAppId(),
                            state.getAreaId(),state.getCallBackUrl(),ConversationService.MAX_DURATION,null,state.getUserdata());
                }else if(state.getBusinessData().get(CallCenterUtil.DIRECT_HOT_FIELD) != null){
                    //收码
                    Map<String, Object> receive_params = new MapBuilder<String,Object>()
                            .putIfNotEmpty("res_id",state.getResId())
                            .putIfNotEmpty("valid_keys","0123456789")
                            .putIfNotEmpty("max_keys","12")
                            .putIfNotEmpty("finish_keys","")
                            .putIfNotEmpty("first_key_timeout","10")
                            .putIfNotEmpty("continues_keys_timeout","3")
                            .putIfNotEmpty("user_data",call_id)
                            .put("areaId",state.getAreaId())
                            .build();

                    RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_RECEIVE_DTMF_START, receive_params);
                    if(!businessStateService.closed(call_id)) {
                        rpcCaller.invoke(sessionContext, rpcrequest);
                        //热线收码标记
                        businessStateService.updateInnerField(call_id,CallCenterUtil.DIRECT_RECEIVE_ING_FIELD,"1");
                    }
                }
            }catch (Throwable t){
                logger.info("",t);
                hangup(state.getResId(),call_id,state.getAreaId());
            }finally {
                //删除等待应答标记，这行代码不能改变顺序
                businessStateService.deleteInnerField(call_id,IVRActionService.IVR_ANSWER_WAITTING_FIELD);
            }
        }else{
            //删除等待应答标记，这行代码不能改变顺序
            businessStateService.deleteInnerField(call_id,IVRActionService.IVR_ANSWER_WAITTING_FIELD);
            ivrActionService.doAction(call_id,null);
        }

        return res;
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
