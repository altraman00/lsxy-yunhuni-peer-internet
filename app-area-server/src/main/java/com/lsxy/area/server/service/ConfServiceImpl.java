package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class ConfServiceImpl implements ConfService {

    private static final Logger logger = LoggerFactory.getLogger(ConfServiceImpl.class);

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private CalBillingService calBillingService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Override
    public String create(String ip, String appId, Integer maxDuration, Integer maxParts,
                         Boolean recording, Boolean autoHangup, String bgmFile, String userData) throws YunhuniApiException {
        App app = appService.findById(appId);
        String tenantId = app.getTenant().getId();

        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }

        BigDecimal balance = calBillingService.getBalance(tenantId);
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException();
        }
        String confId = UUIDGenerator.uuid();
        Map<String, Object> map = new MapBuilder<String,Object>()
                                .putIfNotEmpty("user_data",confId)
                                .putIfNotEmpty("max_seconds",maxDuration)
                                .putIfNotEmpty("bg_file",bgmFile)
                                .putIfNotEmpty("release_threshold",0).build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF, map);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据
        BusinessState state = new BusinessState(tenantId,app.getId(),confId,"sys_conf",
                userData,new MapBuilder<String,Object>()
                .put("max_seconds",maxDuration)//会议最大持续时长
                .put("max_parts",maxParts)//最大与会数
                .put("parts_num",0)//与会数
                .put("auto_hangup",autoHangup)//会议结束是否自动挂断
                .put("recording",recording)//是否自动启动录音
                .build());
        businessStateService.save(state);
        return confId;
    }

    @Override
    public boolean dismiss(String ip, String appId, String confId) throws YunhuniApiException {
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }
        BusinessState state = businessStateService.get(confId);

        if(state == null){
            throw new ConfNotExistsException();
        }

        if(!appId.equals(state.getAppId())){
            //不能跨app操作
            throw new ConfNotExistsException();
        }
        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("user_data",confId)
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

        if(apiGwRedBlankNumService.isRedOrBlankNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        String tenantId = app.getTenant().getId();
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }

        BigDecimal balance = calBillingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
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

        String callId = UUIDGenerator.uuid();
        //TODO
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",to+"@"+lineGateway.getIp()+":"+lineGateway.getPort())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_answer_seconds",maxDuration)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState(tenantId,app.getId(),callId,"sys_conf",null
                ,new MapBuilder<String,Object>()
                .put("max_seconds",state.getBusinessData().get("max_seconds"))//最大时间,默认与会议一只
                .put("conf_id",confId)//所属会议
                .put("play_file",playFile)//加入后在会议播放这个文件
                .put("voice_mode",voiceMode)//加入后的声音模式
                .put("dial_voice_stop_cond",dialVoiceStopCond)//自定义拨号音停止播放条件。0：振铃停止；1：接听或者挂断停止。
                .build());
        businessStateService.save(callstate);
        return callId;
    }

    @Override
    public boolean join(String ip, String appId, String confId, String callId, Integer maxDuration, String playFile, Integer voiceMode) throws YunhuniApiException{
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }

        BigDecimal balance = calBillingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException();
        }
        return this.confEnter(callId,confId);
    }

    @Override
    public boolean quit(String ip, String appId, String confId, String callId) throws YunhuniApiException {
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
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
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",call_state.getResId())
                .putIfNotEmpty("conf_res_id",conf_state.getResId())
                .putIfNotEmpty("user_data",callId)
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
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }
        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }

        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("file",StringUtils.join(playFiles,"|"))
                .putIfNotEmpty("user_data",confId)
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
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }
        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }

        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("user_data",confId)
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
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }
        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }

        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("max_seconds",maxDuration)
                .putIfNotEmpty("record_file",UUIDGenerator.uuid())
                .putIfNotEmpty("user_data",confId)
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
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException();
        }
        BusinessState conf_state = businessStateService.get(confId);

        if(conf_state == null || conf_state.getResId() == null){
            throw new IllegalArgumentException();
        }

        Map<String,Object> params = new MapBuilder<String,Object>()
                .put("res_id",conf_state.getResId())
                .put("user_data",confId)
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
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
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
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",conf_state.getResId())
                .putIfNotEmpty("call_res_id",call_state.getResId())
                .putIfNotEmpty("mode",voiceMode)
                .putIfNotEmpty("user_data",callId)
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
    public boolean confEnter(String call_id, String conf_id) throws YunhuniApiException {
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

        Integer max_seconds = 0;
        Integer voice_mode = 1;
        Integer volume = 100;
        String play_file = "";
        if(call_business!=null){
            if(call_business.get("max_seconds")!=null){
                max_seconds = (Integer)call_business.get("max_seconds");
            }else if(conf_business.get("max_seconds")!=null){
                max_seconds = (Integer)conf_business.get("max_seconds");
            }
            if(call_business.get("voice_mode")!=null){
                voice_mode = (Integer) call_business.get("voice_mode");
            }
            if(call_business.get("volume")!=null){
                volume = (Integer) call_business.get("volume");
            }
            if(call_business.get("play_file")!=null){
                play_file = (String) call_business.get("play_file");
            }
        }
        Map<String, Object> params = new MapBuilder<String,Object>()
                                    .putIfNotEmpty("res_id",call_state.getResId())
                                    .putIfNotEmpty("conf_res_id",conf_state.getResId())
                                    .putIfNotEmpty("max_seconds",max_seconds)
                                    .putIfNotEmpty("voice_mode",voice_mode)
                                    .putIfNotEmpty("volume",volume)
                                    .putIfNotEmpty("play_file",play_file)
                                    .putIfNotEmpty("user_data",call_id)
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
}
