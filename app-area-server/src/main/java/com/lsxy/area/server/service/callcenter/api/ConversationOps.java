package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.callcenter.AgentIdCallReference;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.area.server.util.SipUrlUtil;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
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
public class ConversationOps implements com.lsxy.call.center.api.service.ConversationOps {

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
    private PlayFileUtil playFileUtil;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Autowired
    private CallbackUrlUtil callbackUrlUtil;

    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;

    @Override
    public boolean dismiss(String subaccountId, String ip, String appId, String conversationId) throws YunhuniApiException{
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
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
                                .put("conversationId",conversationId)
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
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,businessStateService.subaccountId(conversationId))){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
            );
        }
        return conversationService.dismiss(appId, conversationId);
    }

    @Override
    public boolean setVoiceMode(String subaccountId, String ip, String appId, String conversationId, String agentId, Integer voiceMode) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("agentId",agentId)
            );
        }
        if(StringUtils.isBlank(agentId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("agentId",agentId)
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
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("agentId",agentId)
                            .put("callId",callId)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,businessStateService.subaccountId(callId))){
            throw new CallNotExistsException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("agentId",agentId)
                            .put("callId",callId)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,businessStateService.subaccountId(conversationId))){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("agentId",agentId)
                            .put("callId",callId)
            );
        }
        return conversationService.setVoiceMode(conversationId,callId,voiceMode);
    }

    @Override
    public boolean inviteAgent(String subaccountId, String ip, String appId, String conversationId, String enqueue, Integer voiceMode) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
            );
        }
        if(StringUtils.isBlank(enqueue)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("enqueue_xml",enqueue)
            );
        }
        EnQueue enQueue = null;
        try {
            Document document = DocumentHelper.parseText(enqueue);
            if(ivrActionService.validateXMLSchema(document)){
                enQueue = EnQueueDecoder.decode(enqueue);
            }
        } catch (DocumentException e) {
            throw new RequestIllegalArgumentException(e);
        }
        if(enQueue == null){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversationId",conversationId)
                            .put("enqueue_xml",enqueue)
            );
        }
        enQueue.setVoice_mode(voiceMode);

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

        BusinessState conversation_state = businessStateService.get(conversationId);
        if(conversation_state == null){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
            );
        }
        if(conversation_state.getResId() == null){
            throw new SystemBusyException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
            );
        }
        if(conversation_state.getClosed()!= null && conversation_state.getClosed()){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("conversationstate",conversation_state)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,conversation_state.getSubaccountId())){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId)
                            .put("conversation_id",conversationId)
                            .put("conversationstate",conversation_state)
            );
        }
        /**排队都是在呼叫上排队，这里是在交谈上排队，所以创建一个虚拟的呼叫call，兼容排队的逻辑**/
        String callId = UUIDGenerator.uuid();
        BusinessState state = new BusinessState.Builder()
                .setTenantId(conversation_state.getTenantId())
                .setAppId(app.getId())
                .setSubaccountId(subaccountId)
                .setId(callId)
                .setResId(null)
                .setType(BusinessState.TYPE_CC_CONVERSATION_SHADOW_CALL)
                .setCallBackUrl(callbackUrlUtil.get(app,subaccountId))
                .setUserdata(conversation_state.getUserdata())
                .setAreaId(conversation_state.getAreaId())
                .setLineGatewayId(conversation_state.getLineGatewayId())
                .setBusinessData(new MapBuilder<String,String>()
                        .put(BusinessState.REF_RES_ID,conversation_state.getBusinessData().get(BusinessState.REF_RES_ID))
                        .putIfNotEmpty("from", SipUrlUtil.extractTelnum(conversation_state.getBusinessData().get(CallCenterUtil.CONVERSATION_SYSNUM_FIELD)))
                        .putIfNotEmpty(CallCenterUtil.CALLCENTER_FIELD,conversation_state.getBusinessData().get(CallCenterUtil.CALLCENTER_FIELD))
                        .putIfNotEmpty(CallCenterUtil.ENQUEUE_START_TIME_FIELD,""+new Date().getTime())
                        .putIfNotEmpty(CallCenterUtil.CONDITION_ID_FIELD,enQueue.getRoute().getCondition()!=null?enQueue.getRoute().getCondition().getId():null)
                        .putIfNotEmpty("user_data",enQueue.getUser_data())
                        .build())
                .build();
        businessStateService.save(state);

        if(StringUtils.isNotBlank(enQueue.getWait_voice())){
            String playWait = enQueue.getWait_voice();
            try {
                //播放排队等待音
                playWait = playFileUtil.convert(state.getTenantId(),state.getAppId(),playWait);
                Map<String, Object> _params = new MapBuilder<String,Object>()
                        .putIfNotEmpty("res_id",conversation_state.getResId())
                        .putIfNotEmpty("content", JSONUtil2.objectToJson(new Object[][]{new Object[]{playWait,0,""}}))
                        .putIfNotEmpty("user_data",conversationId)
                        .putIfNotEmpty("is_loop",true)
                        .put("areaId",conversation_state.getAreaId())
                        .build();
                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_PLAY, _params);
                rpcCaller.invoke(sessionContext, rpcrequest,true);
                businessStateService.updateInnerField(conversationId,CallCenterUtil.PLAYWAIT_FIELD,playWait);
            } catch (Throwable t) {
                logger.error("播放排队等待音失败",t);
            }
        }

        //开始排队
        try {
            enQueueService.lookupAgent(conversation_state.getTenantId(),
                    conversation_state.getAppId(),state.getSubaccountId(),
                    state.getBusinessData().get("from"),
                    callId, enQueue,CallCenterUtil.QUEUE_TYPE_INVITE_AGENT,conversationId);
        }catch (Throwable t){
            logger.info("排队找坐席出错",t);
            deQueueService.fail(conversation_state.getTenantId(),
                    conversation_state.getAppId(),callId,null,CallCenterUtil.QUEUE_TYPE_INVITE_AGENT,"排队找坐席出错",conversationId);
            businessStateService.delete(callId);
            throw t;
        }
        return true;
    }

    @Override
    public String inviteOut(String subaccountId, String ip, String appId, String conversationId, String from,
                            String to, Integer maxDial, Integer maxDuration, Integer voiceMode) throws YunhuniApiException {
        if(StringUtils.isBlank(conversationId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                    .put("appId",appId).put("conversationId",conversationId)
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
                                .put("appId",appId).put("ip",ip)
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

        BusinessState conversation_state = businessStateService.get(conversationId);
        if(conversation_state == null){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId).put("conversationId",conversationId)
            );
        }
        if(conversation_state.getResId() == null){
            throw new SystemBusyException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId).put("conversationId",conversationId)
                            .put("conversation_sate",conversation_state)
            );
        }
        if(conversation_state.getClosed()!= null && conversation_state.getClosed()){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId).put("conversationId",conversationId)
                            .put("conversation_sate",conversation_state)
            );
        }
        if(!apiCertificateSubAccountService.subaccountCheck(subaccountId,conversation_state.getSubaccountId())){
            throw new ConversationNotExistException(
                    new ExceptionContext().put("subaccountId",subaccountId)
                            .put("appId",appId).put("conversationId",conversationId)
                            .put("conversation_sate",conversation_state)
            );
        }
        return conversationService.inviteOut(subaccountId,appId,
                    conversation_state.getBusinessData().get(BusinessState.REF_RES_ID),
                    conversationId,from,to,maxDuration,maxDial,null,voiceMode,conversation_state.getUserdata());
    }
}
