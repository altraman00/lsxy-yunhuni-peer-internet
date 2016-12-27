package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.area.server.util.RecordFileUtil;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.agentserver.EnterConversationEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by liuws on 2016/11/18.
 */
@Component
public class ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

    /**最大成员数**/
    public static final int MAX_PARTS = 8;

    /**交谈最大时长**/
    public static final int MAX_DURATION = 60 * 60 * 6;

    /**key的过期时间 秒**/
    public static final int EXPIRE = MAX_DURATION + 60 * 10;

    private static final String CONVERSATION_PARTS_COUNTER_KEY_PREFIX = "callcenter.conversation_parts_";

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private AppService appService;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private CallConversationService callConversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterQueueService callCenterQueueService;

    @Autowired
    private CallCenterUtil callCenterUtil;

    @Autowired
    private MQService mqService;

    @Value(value = "${app.cc.opensips.ip}")
    private String sip_address;

    public BaseEnQueue getEnqueue(String queueId){
        BaseEnQueue enqueue = null;
        try{
            CallCenterQueue ccQueue = callCenterQueueService.findById(queueId);
            if(ccQueue != null && StringUtil.isNotEmpty(ccQueue.getEnqueue())){
                enqueue = JSONUtil2.fromJson(ccQueue.getEnqueue(),BaseEnQueue.class);
            }
        }catch (Throwable t){
            logger.error("获取排队信息失败",t);
        }
        return enqueue;
    }

    /**
     * 判断callId是否conversation的发起者
     * @return
     */
    public String isInitiator(String conversation,String callId){
        if(StringUtils.isEmpty(conversation) || StringUtils.isEmpty(callId)){
            return CallCenterConversationMember.INITIATOR_FALSE;
        }
        BusinessState state = businessStateService.get(conversation);
        if(state != null && state.getBusinessData()!= null){
            String initiator = state.getBusinessData().get(CallCenterUtil.INITIATOR_FIELD);
            if(initiator != null && initiator.equals(callId)){
                return CallCenterConversationMember.INITIATOR_TRUE;
            }
        }
        return CallCenterConversationMember.INITIATOR_FALSE;
    }

    public boolean isInitiatorBool(String conversation,String callId){
        return CallCenterConversationMember.INITIATOR_TRUE.equals(isInitiator(conversation, callId));
    }

    public boolean isCC(String callId){
        if(StringUtils.isEmpty(callId)){
            return false;
        }
        BusinessState state = businessStateService.get(callId);
        return isCC(state);
    }

    public boolean isCC(BusinessState state){
        if(state != null && state.getBusinessData()!= null){
            String iscc = state.getBusinessData().get(CallCenterUtil.ISCC_FIELD);
            return iscc !=null && iscc.equals(CallCenterUtil.ISCC_TRUE);
        }
        return false;
    }

    public boolean isPlayWait(String callId){
        if(StringUtils.isEmpty(callId)){
            return false;
        }
        BusinessState state = businessStateService.get(callId);
        if(state != null && state.getBusinessData()!= null){
            String playWait = state.getBusinessData().get(CallCenterUtil.IS_PLAYWAIT_FIELD);
            return playWait !=null && playWait.equals(CallCenterUtil.IS_PLAYWAIT_TRUE);
        }
        return false;
    }

    public String getInitiator(String conversation){
        BusinessState state = businessStateService.get(conversation);
        if(state != null && state.getBusinessData()!= null){
            return state.getBusinessData().get(CallCenterUtil.INITIATOR_FIELD);
        }
        return null;
    }

    public String getCallCenter(String callId){
        BusinessState state = businessStateService.get(callId);
        return getCallCenter(state);
    }

    public String getCallCenter(BusinessState state){
        if(state == null){
            return null;
        }
        if(state.getBusinessData() == null){
            return null;
        }
        return state.getBusinessData().get(CallCenterUtil.CALLCENTER_FIELD);
    }

    /**
     * 发起交谈
     * @param initiator
     * @param appId
     * @param maxDuration
     * @return
     * @throws YunhuniApiException
     */
    public String create(String id,String ref_res_id,String initiator,String tenantId,String appId, String areaId,String callBackUrl, Integer maxDuration) throws YunhuniApiException {
        if(maxDuration == null || maxDuration > MAX_DURATION){
            maxDuration = MAX_DURATION;
        }
        Map<String, Object> map = new MapBuilder<String,Object>()
                .putIfNotEmpty("user_data",id)
                //.putIfNotEmpty("record_file", RecordFileUtil.getRecordFileUrl(tenantId, appId))
                .put("max_seconds",maxDuration)
                .putIfNotEmpty("areaId",areaId)
                .putIfNotEmpty(BusinessState.REF_RES_ID,ref_res_id)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF, map);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据
        BusinessState state = new BusinessState.Builder()
                .setTenantId(tenantId)
                .setAppId(appId)
                .setId(id)
                .setType(BusinessState.TYPE_CC_CONVERSATION)
                .setCallBackUrl(callBackUrl)
                .setAreaId(areaId)
                .setBusinessData(new MapBuilder<String,String>()
                        .putIfNotEmpty(BusinessState.REF_RES_ID,ref_res_id)
                        .putIfNotEmpty(CallCenterUtil.INITIATOR_FIELD,initiator)//交谈发起者的callid
                        .putIfNotEmpty(CallCenterUtil.CALLCENTER_FIELD,getCallCenter(initiator))
                        .putIfNotEmpty("max_seconds",maxDuration.toString())//交谈最大持续时长
                        .build())
                .build();
        businessStateService.save(state);
        try{
            CallCenterConversation conversation = new CallCenterConversation();
            conversation.setId(id);
            conversation.setAppId(appId);
            conversation.setTenantId(tenantId);
            conversation.setRelevanceId(initiator);
            conversation.setStartTime(new Date());
            callCenterConversationService.save(conversation);
        }catch (Throwable t){
            logger.error("保存CallCenterConversation失败",t);
        }
        return id;
    }


    /**
     * 解散交谈
     * @param appId
     * @param conversationId
     * @return
     * @throws YunhuniApiException
     */
    public boolean dismiss(String appId, String conversationId) throws YunhuniApiException {
        BusinessState state = businessStateService.get(conversationId);
        if(state == null || (state.getClosed() != null && state.getClosed())){
            throw new ConversationNotExistException();
        }
        if(!appId.equals(state.getAppId())){
            //不能跨app操作
            throw new ConversationNotExistException();
        }
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("user_data",conversationId)
                .putIfNotEmpty("areaId",state.getAreaId())
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RELEASE, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest,true);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }


    /**
     * 邀请坐席加入交谈
     * @param appId
     * @param conversationId
     * @param maxDuration
     * @param maxDialDuration
     * @return
     * @throws YunhuniApiException
     */
    public String inviteAgent(String appId,String ref_res_id,String initiator, String conversationId,String agentId,String agentName,String extension,
                         String telnum,String type,String user,
                          Integer maxDuration, Integer maxDialDuration) throws YunhuniApiException{
        String callId = UUIDGenerator.uuid();
        App app = appService.findById(appId);
        String from = null;
        String to = null;
        String areaId = null;
        String lineId = null;
        if(AppExtension.TYPE_TELPHONE.equals(type)){
            AreaAndTelNumSelector.Selector selector =
                    areaAndTelNumSelector.getTelnumberAndAreaId(app,null,telnum);
            from = selector.getOneTelnumber();
            to = selector.getToUri();
            areaId = selector.getAreaId();
            lineId = selector.getLineId();
        }else{
            areaId = areaAndTelNumSelector.getAreaId(app);
            from = "10000@"+areaId+".area.oneyun.com";
            to = user + "@" + sip_address;
        }
        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(from);
        callSession.setToNum(to);
        callSession.setApp(app);
        callSession.setTenant(app.getTenant());
        callSession.setRelevanceId(callId);
        callSession.setType(CallSession.TYPE_CALL_CENTER);
        callSession.setResId(null);
        callSession = callSessionService.save(callSession);
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",to)
                .putIfNotEmpty("from_uri",from)
                .put("max_answer_seconds",maxDuration, IVRActionService.MAX_DURATION_SEC)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .put("areaId",areaId)
                .putIfNotEmpty(BusinessState.REF_RES_ID,ref_res_id)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState.Builder()
                .setTenantId(app.getTenant().getId())
                .setAppId(app.getId())
                .setId(callId)
                .setType(BusinessState.TYPE_CC_AGENT_CALL)
                .setCallBackUrl(app.getUrl())
                .setAreaId(areaId)
                .setLineGatewayId(lineId)
                .setBusinessData(new MapBuilder<String,String>()
                        .putIfNotEmpty(BusinessState.REF_RES_ID,ref_res_id)
                        .putIfNotEmpty(CallCenterUtil.CONVERSATION_FIELD,conversationId)
                        .putIfNotEmpty(CallCenterUtil.AGENT_ID_FIELD,agentId)
                        .putIfNotEmpty(CallCenterUtil.AGENT_NAME_FIELD,agentName)
                        .putIfNotEmpty(CallCenterUtil.AGENT_EXTENSION_FIELD,extension)
                        .putIfNotEmpty(CallCenterUtil.CALLCENTER_FIELD,getCallCenter(initiator))
                        .putIfNotEmpty("from",from)
                        .putIfNotEmpty("to",to)
                        .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                        .build())
                .build();
        businessStateService.save(callstate);
        return callId;
    }

    /**
     * 邀请外线
     * @param appId
     * @param conversationId
     * @param from
     * @param to
     * @param maxDuration
     * @param maxDialDuration
     * @param playFile
     * @param voiceMode
     * @return
     * @throws YunhuniApiException
     */
    public String inviteOut(String appId,String ref_res_id, String conversationId,
                              String from, String to, Integer maxDuration, Integer maxDialDuration,
                              String playFile, Integer voiceMode) throws YunhuniApiException{
        App app = appService.findById(appId);
        AreaAndTelNumSelector.Selector selector =
                areaAndTelNumSelector.getTelnumberAndAreaId(app,from,to);

        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber();
        String lineId = selector.getLineId();

        String callId = UUIDGenerator.uuid();
        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(oneTelnumber);
        callSession.setToNum(selector.getToUri());
        callSession.setApp(app);
        callSession.setTenant(app.getTenant());
        callSession.setRelevanceId(callId);
        callSession.setType(CallSession.TYPE_CALL_CENTER);
        callSession.setResId(null);
        callSession = callSessionService.save(callSession);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",selector.getToUri())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .put("max_answer_seconds",maxDuration, IVRActionService.MAX_DURATION_SEC)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .put("areaId",areaId)
                .putIfNotEmpty(BusinessState.REF_RES_ID,ref_res_id)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }

        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState.Builder()
                .setTenantId(app.getTenant().getId())
                .setAppId(app.getId())
                .setId(callId)
                .setType(BusinessState.TYPE_CC_OUT_CALL)
                .setCallBackUrl(app.getUrl())
                .setAreaId(areaId)
                .setLineGatewayId(lineId)
                .setBusinessData(new MapBuilder<String,String>()
                        .putIfNotEmpty(BusinessState.REF_RES_ID,ref_res_id)
                        .putIfNotEmpty(CallCenterUtil.CONVERSATION_FIELD,conversationId)
                        .putIfNotEmpty(CallCenterUtil.CALLCENTER_FIELD,getCallCenter(conversationId))
                        .putIfNotEmpty("from",oneTelnumber)
                        .putIfNotEmpty("to",to)
                        .putIfNotEmpty("play_file",playFile)//加入后在交谈中播放这个文件
                        .putIfNotEmpty(CallCenterUtil.PARTNER_VOICE_MODE_FIELD,voiceMode==null?null:voiceMode.toString())//加入后的声音模式
                        .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                        .build())
                .build();
        businessStateService.save(callstate);
        return callId;
    }

    /**
     * 加入交谈
     */
    public boolean join(String conversationId, String callId, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException{

        if(this.outOfParts(conversationId)){
            throw new OutOfConversationMaxPartsException();
        }
        return this.enter(callId,conversationId,maxDuration,playFile,voiceMode);
    }


    private boolean enter(String call_id, String conversation_id, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("开始呼叫加入交谈call_id={},conversation_id={},maxDuration={},playFile={},voiceMode={}",
                    call_id,conversation_id,maxDuration,playFile,voiceMode);
        }
        BusinessState call_state = businessStateService.get(call_id);
        BusinessState conversation_state = businessStateService.get(conversation_id);

        if(call_state ==null || call_state.getResId() == null){
            throw new SystemBusyException();
        }

        if(call_state.getClosed()!= null && call_state.getClosed()){
            throw new SystemBusyException();
        }

        if(conversation_state != null &&
                conversation_state.getResId() == null &&
                (conversation_state.getClosed() == null ||
                        !conversation_state.getClosed())){
            logger.info("交谈={}尚未初始化完成，callid={}",conversation_id,call_id);
            mqService.publish(new EnterConversationEvent(call_id,conversation_id,maxDuration,playFile,voiceMode));
            return false;
        }

        if(conversation_state == null || conversation_state.getResId() == null){
            throw new SystemBusyException();
        }

        if(conversation_state.getClosed()!= null && conversation_state.getClosed()){
            throw new SystemBusyException();
        }

        if(!call_state.getAppId().equals(conversation_state.getAppId())){
            //不合法的参数
            throw new IllegalArgumentException();
        }

        Map<String,String> call_business=call_state.getBusinessData();
        Map<String,String> conversation_business=conversation_state.getBusinessData();

        Integer max_seconds = maxDuration == null ? 0 : maxDuration;
        Integer voice_mode = voiceMode == null ? CallCenterConversationMember.MODE_DEFAULT : voiceMode;
        String play_file = playFile == null ? "" : playFile;

        if(call_business != null && call_business.get("max_seconds")!=null){
            max_seconds = Integer.parseInt(call_business.get("max_seconds"));
        }else if(conversation_business != null && conversation_business.get("max_seconds")!=null){
            max_seconds = Integer.parseInt(conversation_business.get("max_seconds"));
        }

        if(call_business != null && call_business.get(CallCenterUtil.PARTNER_VOICE_MODE_FIELD)!=null){
            voice_mode = Integer.parseInt(call_business.get(CallCenterUtil.PARTNER_VOICE_MODE_FIELD));
        }

        if(call_business != null && call_business.get("play_file")!=null){
            play_file = call_business.get("play_file");
        }

        play_file = playFileUtil.convert(conversation_state.getTenantId(),conversation_state.getAppId(),play_file);
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",call_state.getResId())
                .putIfNotEmpty("conf_res_id",conversation_state.getResId())
                .put("max_seconds",max_seconds,MAX_DURATION)
                .putIfNotEmpty(CallCenterUtil.PARTNER_VOICE_MODE_FIELD,voice_mode)
                .putIfNotEmpty("play_file",play_file)
                .putIfNotEmpty("user_data",call_id)
                .putIfNotEmpty("areaId", conversation_state.getAreaId())
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_ENTER, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        List<String> innerFields = new ArrayList<>();
        if(call_business.get(CallCenterUtil.CONVERSATION_FIELD) == null){
            innerFields.add(CallCenterUtil.CONVERSATION_FIELD);
            innerFields.add(conversation_id);
        }
        innerFields.add(CallCenterUtil.PARTNER_VOICE_MODE_FIELD);
        innerFields.add(voice_mode.toString());
        businessStateService.updateInnerField(call_id,innerFields);
        if(logger.isDebugEnabled()){
            logger.debug("完成呼叫加入交谈call_id={},conversation_id={},maxDuration={},playFile={},voiceMode={}",
                    call_id,conversation_id,maxDuration,playFile,voice_mode);
        }
        return true;
    }


    /**
     * 主动退出会议，调用cti命令
     * @param conversationId
     * @param callId
     */
    public void exit(String conversationId,String callId){
        if(logger.isDebugEnabled()){
            logger.info("调用退出会议命令conversationId={},callId={}",conversationId,callId);
        }
        BusinessState call_state = businessStateService.get(callId);
        BusinessState conversation_state = businessStateService.get(conversationId);
        if(call_state == null || call_state.getResId() == null){
            logger.info("(call_state == null || call_state.getResId() == null)conversationId={},callId={}",conversationId,callId);
            return;
        }
        if(call_state.getClosed() != null && call_state.getClosed()){
            return;
        }
        if(conversation_state == null || conversation_state.getResId() == null){
            logger.info("(conversation_state == null || conversation_state.getResId() == null)conversationId={},callId={}",conversationId,callId);
            return;
        }
        if(conversation_state.getClosed() != null && conversation_state.getClosed()){
            return;
        }
        if(!call_state.getAppId().equals(conversation_state.getAppId())){
            logger.info("(!call_state.getAppId().equals(conversation_state.getAppId()))conversationId={},callId={}",conversationId,callId);
            return;
        }
        try {
            Map<String,Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",call_state.getResId())
                    .putIfNotEmpty("conf_res_id",conversation_state.getResId())
                    .putIfNotEmpty("user_data",callId)
                    .putIfNotEmpty("areaId",call_state.getAreaId())
                    .build();
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_EXIT, params);
            rpcCaller.invoke(sessionContext, rpcrequest,true);
        } catch (Throwable e) {
            logger.error("调用将呼叫退出会议失败",e);
        }
    }

    public void startRecord(String conversationId){
        BusinessState conversation_state = businessStateService.get(conversationId);
        startRecord(conversation_state);
    }

    public void startRecord(BusinessState state){
        if(state == null || state.getResId() == null){
            return;
        }
        if(state.getClosed() != null && state.getClosed()){
            return;
        }
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("max_seconds",MAX_DURATION)
                .putIfNotEmpty("record_file", RecordFileUtil.getRecordFileUrl(state.getTenantId(),state.getAppId()))
                .putIfNotEmpty("user_data",state.getId())
                .putIfNotEmpty("areaId",state.getAreaId())
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            logger.error("启动交谈录音失败",e);
        }
    }

    public void setVoiceMode(String areaId,String conversationId, String callId, Integer voiceMode) throws YunhuniApiException {
        BusinessState call_state = businessStateService.get(callId);
        BusinessState conversation_state = businessStateService.get(conversationId);

        if(call_state ==null || call_state.getResId() == null){
            throw new SystemBusyException();
        }

        if(call_state.getClosed()!= null && call_state.getClosed()){
            throw new SystemBusyException();
        }

        if(conversation_state == null || conversation_state.getResId() == null){
            throw new SystemBusyException();
        }

        if(conversation_state.getClosed()!= null && conversation_state.getClosed()){
            throw new SystemBusyException();
        }

        if(!call_state.getAppId().equals(conversation_state.getAppId())){
            throw new IllegalArgumentException();
        }
        if(voiceMode ==null){
            throw new IllegalArgumentException();
        }
        if(voiceMode.intValue() != CallCenterConversationMember.MODE_I_O&&
                voiceMode.intValue() != CallCenterConversationMember.MODE_I&&
                voiceMode.intValue() != CallCenterConversationMember.MODE_O&&
                voiceMode.intValue() != CallCenterConversationMember.MODE_N){
            throw new IllegalArgumentException();
        }
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conversation_state.getResId())
                .putIfNotEmpty("call_res_id",call_state.getResId())
                .putIfNotEmpty("mode",voiceMode)
                .putIfNotEmpty("user_data",callId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_SET_PART_VOICE_MODE, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        businessStateService.updateInnerField(callId,CallCenterUtil.PARTNER_VOICE_MODE_FIELD,voiceMode.toString());
    }

    /**
     * 接收到呼叫退出会议事件后，处理成员和呼叫所在的交谈
     * @param conversationId
     * @param callId
     */
    public void logicExit(String conversationId,String callId){
        BusinessState call_state = businessStateService.get(callId);
        BusinessState conversation_state = businessStateService.get(conversationId);
        if(call_state == null || call_state.getResId() == null){
            logger.info("(call_state == null || call_state.getResId() == null)conversationId={},callId={}",conversationId,callId);
            return;
        }
        if(conversation_state == null || conversation_state.getResId() == null){
            logger.info("(conversation_state == null || conversation_state.getResId() == null)conversationId={},callId={}",conversationId,callId);
            return;
        }
        if(!call_state.getAppId().equals(conversation_state.getAppId())){
            logger.info("(!call_state.getAppId().equals(conversation_state.getAppId()))conversationId={},callId={}",conversationId,callId);
            return;
        }
        try{
            CallCenterConversationMember member = callCenterConversationMemberService.findOne(conversationId,callId);
            if(member != null){
                CallCenterConversationMember updateMember = new CallCenterConversationMember();
                updateMember.setEndTime(new Date());
                callCenterConversationMemberService.update(member.getId(),updateMember);
            }
        }catch (Throwable t){
            logger.error("设置交谈成员的结束时间失败",t);
        }
        //交谈成员递减
        this.decrPart(conversationId,callId);

        //TODO 成员大于1且，活动成员只剩一个了
        if(this.size(conversationId) > 1){
            long activeTotal = 1;//TODO 需要获取活动成员
            if(activeTotal == 1){
                //TODO 播放holdvoice
            }
        }
        //退出呼叫所在的交谈
        callConversationService.decrConversation(callId,conversationId);

        if(call_state.getType().equals(BusinessState.TYPE_CC_AGENT_CALL)){
            callCenterUtil.agentExitConversationEvent(call_state.getCallBackUrl(),
                    call_state.getBusinessData().get(CallCenterUtil.AGENT_ID_FIELD),conversationId);
        }

        if(callConversationService.size(callId) > 0){
            //TODO 回到上一次交谈
            if(logger.isDebugEnabled()) {
                logger.debug("回到上一次交谈callid={}", callId);
            }
            return;
        }
        if(BusinessState.TYPE_IVR_INCOMING.equals(call_state.getType())){
            if(call_state.getClosed() == null || !call_state.getClosed()){
                if(logger.isDebugEnabled()){
                    logger.debug("开始重新进入ivr，callid={}",callId);
                }
                ivrActionService.doAction(callId,null);
            }else if(this.size(conversationId) <= 1){
                if(conversation_state.getClosed() == null || !conversation_state.getClosed()){
                    if(logger.isDebugEnabled()){
                        logger.debug("开始呼入ivr挂断后，解散会议，callid={}",callId);
                    }
                    try {
                        this.dismiss(call_state.getAppId(),conversationId);
                    } catch (Throwable e) {
                        logger.error("呼入ivr挂断后，解散会议",e);
                    }
                }
            }
        }else{

            if(call_state.getClosed() == null || !call_state.getClosed()){
                //不是ivr 不需要下一步  直接挂断
                if(logger.isDebugEnabled()) {
                    logger.debug("开始挂断坐席callid={}", callId);
                }
                hangup(call_state.getResId(),callId,call_state.getAreaId());
            }
        }
    }

    /**
     * 呼叫加入交谈成功
     * **/
    public void join(String conversation_id,String call_id){
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            return;
        }
        CallCenterConversation conversation = callCenterConversationService.findById(conversation_id);
        if(conversation==null){
            return;
        }
        Map<String,String> businessData = state.getBusinessData();
        try{
            callConversationService.incrConversation(call_id,conversation_id);
            this.incrPart(conversation_id,call_id);
            CallCenterConversationMember member = new CallCenterConversationMember();
            member.setCallId(call_id);
            member.setRelevanceId(conversation_id);
            member.setStartTime(new Date());
            member.setSessionId(businessData.get(BusinessState.SESSIONID));
            member.setJoinNum(businessData.get("to"));
            member.setIsInitiator(this.isInitiator(conversation_id,call_id));
            if(businessData.get(CallCenterUtil.AGENT_ID_FIELD) != null){
                member.setIsAgent(CallCenterConversationMember.AGENT_FALSE);
            }else{
                member.setIsAgent(CallCenterConversationMember.AGENT_TRUE);
            }
            if(StringUtil.isNotEmpty(businessData.get(CallCenterUtil.PARTNER_VOICE_MODE_FIELD))){
                member.setMode(Integer.parseInt(businessData.get(CallCenterUtil.PARTNER_VOICE_MODE_FIELD)));
            }
            callCenterConversationMemberService.save(member);
        }catch (Throwable t){
            logger.error("处理加入交谈失败",t);
        }
        if(state.getType().equals(BusinessState.TYPE_CC_AGENT_CALL)){
            callCenterUtil.agentEnterConversationEvent(state.getCallBackUrl(),
                    businessData.get(CallCenterUtil.AGENT_ID_FIELD),conversation_id);
        }
        callCenterUtil.conversationPartsChangedEvent(state.getCallBackUrl(),conversation_id);
    }
    private void hangup(String res_id,String call_id,String area_id){
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("user_data",call_id)
                .put("areaId",area_id)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest,true);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
    }

    private String key(String conversation){
        if(StringUtils.isBlank(conversation)){
            throw new IllegalArgumentException("交谈ID不能为null");
        }
        return CONVERSATION_PARTS_COUNTER_KEY_PREFIX + conversation;
    }

    /**
     * 判断是否超出最大成员数
     * @param conversationId
     * @return
     */
    public boolean outOfParts(String conversationId){
        String key = key(conversationId);
        return redisCacheService.ssize(key) >= MAX_PARTS;
    }

    public long size(String conversationId){
        String key = key(conversationId);
        long size = redisCacheService.ssize(key);
        if(logger.isDebugEnabled()){
            logger.debug("{}交谈成员数={}",conversationId,size);
        }
        return size;
    }
    /**
     * 增加交谈成员
     * @param conversationId
     */
    public void incrPart(String conversationId,String callId){
        String key = key(conversationId);
        redisCacheService.sadd(key,callId);
        redisCacheService.expire(key,EXPIRE);
    }

    /**
     * 减少交谈成员
     * @param conversationId
     */
    public void decrPart(String conversationId,String callId){
        String key = key(conversationId);
        redisCacheService.sremove(key,callId);
        redisCacheService.expire(key,EXPIRE);
    }

    /**
     * 获取交谈成员的call_id
     * @param conversationId
     * @return
     */
    public Set<String> getParts(String conversationId){
        String key = key(conversationId);
        Set<String> results = null;
        try{
            results = redisCacheService.smembers(key);
        }catch (Throwable t){
            logger.error("获取交谈成员失败",t);
        }
        return results;
    }

    /**
     * 弹出交谈成员，并清空
     */
    public Set<String> popParts(String conversationId){
        String key = key(conversationId);
        Set<String> results = null;
        try{
            results = redisCacheService.smembers(key);
        }catch (Throwable t){
            logger.error("获取交谈成员失败",t);
        }
        try{
            redisCacheService.del(key);
        }catch (Throwable t){
            logger.info("删除交谈成员缓存失败",t);
        }
        return results;
    }
}
