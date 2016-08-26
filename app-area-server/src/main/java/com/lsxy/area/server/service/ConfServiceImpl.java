package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
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
import java.util.HashMap;
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

    @Autowired(required = false)
    private StasticsCounter cs;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private ServerSessionContext sessionContext;

    @Autowired(required = false)
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired(required = false)
    AppService appService;

    @Autowired(required = false)
    BillingService billingService;

    @Override
    public String create(String ip, String appId, Integer maxDuration, Integer maxParts, Boolean recording, Boolean autoHangup, String bgmFile, String callBackURL, String userData) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException("余额不足");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf");
        }
        String callId = UUIDGenerator.uuid();
        Map<String, Object> map = new HashMap<String,Object>();
        map.put("callId",callId);
        map.put("max_seconds",maxDuration);
        map.put("bg_file",bgmFile);
        map.put("release_threshold",0);

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF, map);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return callId;
    }

    @Override
    public boolean dismiss(String ip, String appId, String confId) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }
        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf.release");
        }
        //TODO 根据confId 获取res_id
        String params = String.format("res_id=%s",confId);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RELEASE, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public String invite(String ip, String appId, String confId,
                         String from, String to, String customFrom,
                         String customTO, Integer maxDuration, Integer maxDialDuration,
                         Integer dialVoiceStopCond, String playFile, Integer voiceMode) throws InvokeCallException{
        //TODO IP黑名单

        if(apiGwRedBlankNumService.isRedOrBlankNum(to)){
            throw new NumberNotAllowToCallException("不能呼叫该号码");
        }

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException("余额不足");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf.invite");
        }

        String callId = UUIDGenerator.uuid();
        String params = String.format("to=%s&from=%s&maxAnswerSec=%d&maxRingSec=%d&callId=%s",
                to, from, maxDuration, maxDialDuration, callId);
        //TODO 调用sys.call 需要把当前的业务。。邀请会议保存到user_data
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return callId;
    }

    @Override
    public boolean join(String ip, String appId, String confId, String callId, Integer maxDuration, String playFile, Integer voiceMode) throws InvokeCallException{
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException("余额不足");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.call.conf_enter");
        }
        //TODO 此处需要根据callId获取呼叫的res_id，confId获取conf_res_id，volume不知道填多少填个5
        String params = String.format("res_id=%s&conf_res_id=%s&max_seconds=%d&voice_mode=%d&volume=%d&play_file=%s",
                callId, confId, maxDuration, voiceMode, 5,playFile);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_ENTER, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public boolean quit(String ip, String appId, String confId, String callId) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.call.conf_exit");
        }
        //TODO 此处需要根据callId获取呼叫的res_id，confId获取conf_res_id
        String params = String.format("res_id=%s&conf_res_id=%s",
                callId, confId);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_CONF_EXIT, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public boolean startPlay(String ip, String appId, String confId, List<String> playFiles) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf.play_start");
        }
        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s&file=%s",
                            confId,StringUtils.join(playFiles,"|"));
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_PLAY, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public boolean stopPlay(String ip, String appId, String confId) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf.play_stop");
        }
        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s",confId);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_PLAY_STOP, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public boolean startRecord(String ip, String appId, String confId, Integer maxDuration) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf.record_start");
        }
        //TODO 此处需要根据CONFId获取呼叫的res_id，录音文件名，录音格式
        String params = String.format("res_id=%s&max_seconds=%d&record_file=%s&record_format=%d",confId,maxDuration, UUID.randomUUID(),6);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public boolean stopRecord(String ip, String appId, String confId) throws InvokeCallException {
        //TODO IP黑名单

        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(whiteList != null && StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("ip不在白名单");
            }
        }

        if(app.getIsSessionService() == null || app.getIsSessionService() != 1){
            throw new AppServiceInvalidException("app没开通会议服务");
        }

        Session session = null;
        try{
            session = sessionContext.getRightSession();
        }catch (RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
        if(session == null){
            throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.conf.record_stop");
        }
        //TODO 此处需要根据confId获取呼叫的res_id
        String params = String.format("res_id=%s",confId);
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD_STOP, params);
        try {
            RPCResponse res = rpcCaller.invokeWithReturn(session, rpcrequest);
            if(logger.isDebugEnabled()){
                logger.debug("响应",JSON.toJSON(res));
            }
            //TODO
        } catch (Exception e) {
            throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
        }
        return true;
    }

    @Override
    public boolean setVoiceMode(String ip, String appId, String confId, String callId, Integer voiceMode) {
        return false;
    }
}
