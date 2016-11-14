package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.framework.api.tenant.model.TenantServiceSwitch;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.Meeting;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.MeetingService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class ConfServiceImpl implements ConfService {

    private static final Logger logger = LoggerFactory.getLogger(ConfServiceImpl.class);

    /**最大与会数**/
    public static final int MAX_PARTS = 8;

    /**key的过期时间 秒**/
    public static final int EXPIRE = 60 * 60 * 12;

    private static final String CONF_PARTS_COUNTER_KEY_PREFIX = "conf_parts_";

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    private boolean isEnableConfService(String tenantId,String appId){
        try {
            TenantServiceSwitch serviceSwitch = tenantServiceSwitchService.findOneByTenant(tenantId);
            if(serviceSwitch != null && (serviceSwitch.getIsSessionService() == null || serviceSwitch.getIsSessionService() != 1)){
                return false;
            }
            App app = appService.findById(appId);
            if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
                return false;
            }
        } catch (Throwable e) {
            logger.error("判断是否开启service失败",e);
            return false;
        }
        return true;
    }

    @Override
    public String create(String ip, String appId, Integer maxDuration, Integer maxParts,
                         Boolean recording, Boolean autoHangup, String bgmFile, String userData) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String tenantId = app.getTenant().getId();


        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(!isEnableConfService(tenantId,appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }
        if(maxParts!=null && maxParts>MAX_PARTS){
            maxParts = MAX_PARTS;
        }
        //TODO
        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(app,null,null);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber().getTelNumber();
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        Meeting meeting = new Meeting();
        meeting.setResId(null);
        meeting.setStartTime(null);
        meeting = meetingService.save(meeting);
        String confId = meeting.getId();

        bgmFile = playFileUtil.convert(tenantId,appId,bgmFile);
        Map<String, Object> map = new MapBuilder<String,Object>()
                                .putIfNotEmpty("user_data",confId)
                                .putIfNotEmpty("max_seconds",maxDuration)
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
                                .setId(confId)
                                .setType("sys_conf")
                                .setUserdata(userData)
                                .setAreaId(areaId)
                                .setLineGatewayId(lineGateway.getId())
                                .setBusinessData(new MapBuilder<String,Object>()
                                        .putIfNotEmpty("max_seconds",maxDuration)//会议最大持续时长
                                        .put("max_parts",maxParts,MAX_PARTS)//最大与会数
                                        .putIfNotEmpty("auto_hangup",autoHangup)//会议结束是否自动挂断
                                        .putIfNotEmpty("recording",recording)//是否自动启动录音
                                        .build())
                                .build();
        businessStateService.save(state);
        return confId;
    }

    @Override
    public boolean dismiss(String ip, String appId, String confId) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        BusinessState state = businessStateService.get(confId);

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
                .putIfNotEmpty("user_data",confId)
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

    @Override
    public String invite(String ip, String appId, String confId,
                         String from, String to, Integer maxDuration, Integer maxDialDuration,
                         Integer dialVoiceStopCond, String playFile, Integer voiceMode) throws YunhuniApiException{

        if(apiGwRedBlankNumService.isRedNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String tenantId = app.getTenant().getId();
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(!isEnableConfService(tenantId,appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        BusinessState state = businessStateService.get(confId);
        if(state == null){
            throw new ConfNotExistsException();
        }
        if(!appId.equals(state.getAppId())){
            //不能跨app操作
            throw new ConfNotExistsException();
        }

        if(this.outOfParts(confId)){
            throw new OutOfConfMaxPartsException();
        }

        String callId = UUIDGenerator.uuid();

        //TODO
        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(app, from,to);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber().getTelNumber();

        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(oneTelnumber);
        callSession.setToNum(to+"@"+lineGateway.getSipProviderIp());
        callSession.setApp(app);
        callSession.setTenant(app.getTenant());
        callSession.setRelevanceId(callId);
        callSession.setType(CallSession.TYPE_VOICE_MEETING);
        callSession.setResId(null);
        callSession = callSessionService.save(callSession);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",to+"@"+lineGateway.getSipProviderIp())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_answer_seconds",maxDuration)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState.Builder()
                                    .setTenantId(tenantId)
                                    .setAppId(app.getId())
                                    .setId(callId)
                                    .setType("sys_conf")
                                    .setAreaId(areaId)
                                    .setLineGatewayId(lineGateway.getId())
                                    .setBusinessData(new MapBuilder<String,Object>()
                                        .putIfNotEmpty("from",oneTelnumber)
                                        .putIfNotEmpty("to",to)
                                        .putIfNotEmpty("max_seconds",maxDuration)//最大时间
                                        .putIfNotEmpty("conf_id",confId)//所属会议
                                        .putIfNotEmpty("play_file",playFile)//加入后在会议播放这个文件
                                        .putIfNotEmpty("voice_mode",voiceMode)//加入后的声音模式
                                        //TODO 这个是什么鬼dial_voice_stop_cond
                                        .putIfNotEmpty("dial_voice_stop_cond",dialVoiceStopCond)//自定义拨号音停止播放条件。0：振铃停止；1：接听或者挂断停止。
                                        .putIfNotEmpty("sessionid",callSession.getId())
                                        .build())
                                    .build();
        businessStateService.save(callstate);
        return callId;
    }

    @Override
    public boolean join(String ip, String appId, String confId, String callId, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException{
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        if(this.outOfParts(confId)){
            throw new OutOfConfMaxPartsException();
        }

        return this.confEnter(callId,confId,maxDuration,playFile,voiceMode);
    }

    @Override
    public boolean quit(String ip, String appId, String confId, String callId) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }
        BusinessState call_state = businessStateService.get(callId);
        BusinessState conf_state = businessStateService.get(confId);
        if(call_state == null || call_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(!call_state.getAppId().equals(conf_state.getAppId())){
            throw new IllegalArgumentException();
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",call_state.getResId())
                .putIfNotEmpty("conf_res_id",conf_state.getResId())
                .putIfNotEmpty("user_data",callId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_EXIT, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }

    @Override
    public boolean startPlay(String ip, String appId, String confId, List<String> playFiles) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        playFiles = playFileUtil.convertArray(app.getTenant().getId(),appId,playFiles);
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("file",StringUtils.join(playFiles,"|"))
                .putIfNotEmpty("user_data",confId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_PLAY, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }

    @Override
    public boolean stopPlay(String ip, String appId, String confId) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }

        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("user_data",confId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_PLAY_STOP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }

    @Override
    public boolean startRecord(String ip, String appId, String confId, Integer maxDuration) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        Map<String,Object> businessData = conf_state.getBusinessData();
        if(maxDuration == null && businessData!=null){
            Object duration = businessData.get("max_seconds");
            if(duration!=null){
                maxDuration = (int)duration;
            }
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("max_seconds",maxDuration)
                //TODO 文件名如何定
                .putIfNotEmpty("record_file",UUIDGenerator.uuid())
                .putIfNotEmpty("user_data",confId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }

    @Override
    public boolean stopRecord(String ip, String appId, String confId) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }
        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("user_data",confId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD_STOP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }

    @Override
    public boolean setVoiceMode(String ip, String appId, String confId, String callId, Integer voiceMode) throws YunhuniApiException {
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

        if(!isEnableConfService(app.getTenant().getId(),appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.sys_conf.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        BusinessState call_state = businessStateService.get(callId);
        BusinessState conf_state = businessStateService.get(confId);
        if(call_state == null || call_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(!call_state.getAppId().equals(conf_state.getAppId())){
            throw new IllegalArgumentException();
        }
        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
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
        return true;

    }

    @Override
    public boolean confEnter(String call_id, String conf_id, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException {
        BusinessState call_state = businessStateService.get(call_id);
        BusinessState conf_state = businessStateService.get(conf_id);
        if(call_state == null || call_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }
        if(!call_state.getAppId().equals(conf_state.getAppId())){
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

        play_file = playFileUtil.convert(conf_state.getTenantId(),conf_state.getAppId(),play_file);
        Map<String, Object> params = new MapBuilder<String,Object>()
                                    .putIfNotEmpty("res_id",call_state.getResId())
                                    .putIfNotEmpty("conf_res_id",conf_state.getResId())
                                    .putIfNotEmpty("max_seconds",max_seconds)
                                    .putIfNotEmpty("voice_mode",voice_mode)
                                    .putIfNotEmpty("play_file",play_file)
                                    .putIfNotEmpty("user_data",call_id)
                                    .putIfNotEmpty("areaId", conf_state.getAreaId())
                                    .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_ENTER, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        if(call_business!=null){
            if(call_business.get("conf_id") == null){
                call_business.put("conf_id",conf_id);
                call_state.setBusinessData(call_business);
                businessStateService.save(call_state);
            }
        }
        return true;
    }


    private String key(String confId){
        if(StringUtils.isBlank(confId)){
            throw new IllegalArgumentException("会议ID不能为null");
        }
        return CONF_PARTS_COUNTER_KEY_PREFIX + confId;
    }
    /**
     * 判断是否达到最大与会数
     * @param confId
     * @return
     */
    @Override
    public boolean outOfParts(String confId){
        String key = key(confId);
        return redisTemplate.opsForSet().size(key) >= MAX_PARTS;
    }

    /**
     * 增加会议成员
     * @param confId
     */
    @Override
    public void incrPart(String confId,String callId){
        String key = key(confId);
        redisTemplate.opsForSet().add(key,callId);
        redisTemplate.expire(key,EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 减少会议成员
     * @param confId
     */
    @Override
    public void decrPart(String confId,String callId){
        String key = key(confId);
        redisTemplate.opsForSet().remove(key,callId);
        redisTemplate.expire(key,EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 获取会议成员的call_id
     * @param confId
     * @return
     */
    @Override
    public Set<String> getParts(String confId){
        String key = key(confId);
        Set<String> results = null;
        try{
            results = redisTemplate.opsForSet().members(key);
        }catch (Throwable t){
            logger.error("获取会议成员失败",t);
        }
        return results;
    }

    /**
     * 弹出会议成员，并清空
     */
    @Override
    public Set<String> popParts(String confId){
        String key = key(confId);
        Set<String> results = null;
        try{
            results = redisTemplate.opsForSet().members(key);
        }catch (Throwable t){
            logger.error("获取会议成员失败",t);
        }
        try{
            redisTemplate.delete(key);
        }catch (Throwable t){
            logger.info("删除会议成员缓存失败",t);
        }
        return results;
    }
}
