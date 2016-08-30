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
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private ServerSessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private BusinessStateService businessStateService;

    @Override
    public String create(String ip, String appId, Integer maxDuration, Integer maxParts, Boolean recording, Boolean autoHangup, String bgmFile, String callBackURL, String userData) throws YunhuniApiException {
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

        BigDecimal balance = billingService.getBalance(tenantId);
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException();
        }
        String confId = UUIDGenerator.uuid();
        Map<String, Object> map = new MapBuilder<String,Object>()
                                .put("user_data",confId)
                                .put("max_seconds",maxDuration)
                                .put("bg_file",bgmFile)
                                .put("release_threshold",0).build();
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
                .put("callback_url",callBackURL)//通知url
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
                .put("res_id",state.getResId())
                .put("user_data",confId)
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
                         String from, String to, String customFrom,
                         String customTO, Integer maxDuration, Integer maxDialDuration,
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

        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
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
        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("to",to)
                .put("from",from)
                .put("maxAnswerSec",maxDuration)
                .put("maxRingSec",maxDialDuration)
                .put("user_data",callId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState(tenantId,app.getId(),callId,"sys_conf_invite",null
                ,new MapBuilder<String,Object>()
                .put("max_seconds",state.getBusinessData().get("max_seconds"))//最大时间,默认与会议一只
                .put("conf_id",confId)//所属会议
                .put("play_file",playFile)//加入后在会议播放这个文件
                .put("voice_mode",voiceMode)//加入后的声音模式
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

        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException();
        }
        //TODO 此处需要根据callId获取呼叫的res_id，confId获取conf_res_id，volume不知道填多少填个5
        String params = String.format("res_id=%s&conf_res_id=%s&max_seconds=%d&voice_mode=%d&volume=%d&play_file=%s",
                callId, confId, maxDuration, voiceMode, 5,playFile);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_ENTER, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
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
        //TODO 此处需要根据callId获取呼叫的res_id，confId获取conf_res_id
        String params = String.format("res_id=%s&conf_res_id=%s",
                callId, confId);
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

        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s&file=%s",
                            confId,StringUtils.join(playFiles,"|"));
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

        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s",confId);
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

        //TODO 此处需要根据CONFId获取呼叫的res_id，录音文件名，录音格式
        String params = String.format("res_id=%s&max_seconds=%d&record_file=%s&record_format=%d",confId,maxDuration, UUID.randomUUID(),6);
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

        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s",confId);
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

        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s&call_res_id=%s&mode=%d",confId,callId,voiceMode);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_SET_PART_VOICE_MODE, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        return true;
    }
}
