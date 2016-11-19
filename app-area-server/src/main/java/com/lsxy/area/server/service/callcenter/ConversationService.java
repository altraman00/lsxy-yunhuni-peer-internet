package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
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
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuws on 2016/11/18.
 */
@Component
public class ConversationService {

    private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

    /**最大成员数**/
    public static final int MAX_PARTS = 8;

    /**key的过期时间 秒**/
    public static final int EXPIRE = 60 * 60 * 12;

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

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    /**
     * 发起交谈
     * @param initiator
     * @param callCenterId
     * @param appId
     * @param maxDuration
     * @param recording
     * @param autoHangup
     * @param bgmFile
     * @return
     * @throws YunhuniApiException
     */
    public String create(String initiator,String callCenterId, String appId, Integer maxDuration,
                         Boolean recording, Boolean autoHangup, String bgmFile) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String tenantId = app.getTenant().getId();

        //TODO
        String areaId = areaAndTelNumSelector.getAreaId(app);

        CallCenterConversation conversation = new CallCenterConversation();
        conversation.setAppId(appId);
        conversation.setTenantId(app.getTenant().getId());
        conversation.setRelevanceId(callCenterId);
        conversation.setStartTime(new Date());
        conversation = callCenterConversationService.save(conversation);
        String conversationId = conversation.getId();

        bgmFile = playFileUtil.convert(tenantId,appId,bgmFile);
        Map<String, Object> map = new MapBuilder<String,Object>()
                .putIfNotEmpty("user_data",conversationId)
                .put("max_seconds",maxDuration,EXPIRE)
                .putIfNotEmpty("bg_file",bgmFile)
                .putIfNotEmpty("areaId",areaId)
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
                .setAppId(app.getId())
                .setId(conversationId)
                .setType("conversation")
                .setAreaId(areaId)
                .setBusinessData(new MapBuilder<String,Object>()
                        .putIfNotEmpty("initiator",initiator)//交谈发起者的callid
                        .put("max_seconds",maxDuration,EXPIRE)//交谈最大持续时长
                        .put("max_parts",MAX_PARTS)//最大与会数
                        .putIfNotEmpty("auto_hangup",autoHangup)//交谈结束是否自动挂断
                        .putIfNotEmpty("recording",recording)//是否自动启动录音
                        .build())
                .build();
        businessStateService.save(state);
        return conversationId;
    }


    /**
     * 解散交谈
     * @param appId
     * @param conversationId
     * @return
     * @throws YunhuniApiException
     */
    public boolean dismiss(String appId, String conversationId) throws YunhuniApiException {
        App app = appService.findById(appId);

        BusinessState state = businessStateService.get(conversationId);

        if(state == null){
            throw new ConfNotExistsException();
        }

        if(!appId.equals(state.getAppId())){
            //不能跨app操作
            throw new ConfNotExistsException();
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("user_data",conversationId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RELEASE, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }


    /**
     * 邀请加入交谈
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
    public String invite(String appId, String conversationId,
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
                .put("areaId ",areaId)
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
                .setType("conversation")
                .setAreaId(areaId)
                .setLineGatewayId(lineId)
                .setBusinessData(new MapBuilder<String,Object>()
                        .putIfNotEmpty("conversation",conversationId)
                        .putIfNotEmpty("from",oneTelnumber)
                        .putIfNotEmpty("to",to)
                        .putIfNotEmpty("play_file",playFile)//加入后在交谈中播放这个文件
                        .putIfNotEmpty("voice_mode",voiceMode)//加入后的声音模式
                        .putIfNotEmpty("sessionid",callSession.getId())
                        .build())
                .build();
        businessStateService.save(callstate);
        return callId;
    }

    /**
     * 加入交谈
     */
    public boolean join(String appId, String conversationId, String callId, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException{

        if(this.outOfParts(conversationId)){
            throw new OutOfConfMaxPartsException();
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
        if(call_state == null || call_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(conversation_state == null || conversation_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(!call_state.getAppId().equals(conversation_state.getAppId())){
            //不合法的参数
            throw new IllegalArgumentException();
        }

        Map<String,Object> call_business=call_state.getBusinessData();
        Map<String,Object> conf_business=call_state.getBusinessData();

        Integer max_seconds = maxDuration == null ? 0 : maxDuration;
        Integer voice_mode = voiceMode == null ? 1 : voiceMode;
        String play_file = playFile == null ? "" : playFile;

        if(call_business != null && call_business.get("max_seconds")!=null){
            max_seconds = (Integer)call_business.get("max_seconds");
        }else if(conf_business != null && conf_business.get("max_seconds")!=null){
            max_seconds = (Integer)conf_business.get("max_seconds");
        }

        if(call_business != null && call_business.get("voice_mode")!=null){
            voice_mode = (Integer) call_business.get("voice_mode");
        }

        if(call_business != null && call_business.get("play_file")!=null){
            play_file = (String) call_business.get("play_file");
        }

        play_file = playFileUtil.convert(conversation_state.getTenantId(),conversation_state.getAppId(),play_file);
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",call_state.getResId())
                .putIfNotEmpty("conf_res_id",conversation_state.getResId())
                .put("max_seconds",max_seconds,EXPIRE)
                .putIfNotEmpty("voice_mode",voice_mode)
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
        if(call_business!=null){
            if(call_business.get("conversation") == null){
                call_business.put("conversation",conversation_id);
                call_state.setBusinessData(call_business);
                businessStateService.save(call_state);
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("完成呼叫加入交谈call_id={},conversation_id={},maxDuration={},playFile={},voiceMode={}",
                    call_id,conversation_id,maxDuration,playFile,voiceMode);
        }
        return true;
    }


    private String key(String conversation){
        if(StringUtils.isBlank(conversation)){
            throw new IllegalArgumentException("交谈ID不能为null");
        }
        return CONVERSATION_PARTS_COUNTER_KEY_PREFIX + conversation;
    }
    /**
     * 判断是否达到最大与会数
     * @param confId
     * @return
     */
    public boolean outOfParts(String confId){
        String key = key(confId);
        return redisCacheService.ssize(key) >= MAX_PARTS;
    }

    /**
     * 增加交谈成员
     * @param confId
     */
    public void incrPart(String confId,String callId){
        String key = key(confId);
        redisCacheService.sadd(key,callId);
        redisCacheService.expire(key,EXPIRE);
    }

    /**
     * 减少交谈成员
     * @param confId
     */
    public void decrPart(String confId,String callId){
        String key = key(confId);
        redisCacheService.sremove(key,callId);
        redisCacheService.expire(key,EXPIRE);
    }

    /**
     * 获取交谈成员的call_id
     * @param confId
     * @return
     */
    public Set<String> getParts(String confId){
        String key = key(confId);
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
    public Set<String> popParts(String confId){
        String key = key(confId);
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
