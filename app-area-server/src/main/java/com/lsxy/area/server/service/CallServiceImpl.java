package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.area.api.*;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class CallServiceImpl implements CallService {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);

    @Autowired(required = false)
    private StasticsCounter cs;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    BusinessStateService businessStateService;

    @Value("${area.agent.client.cti.sip.host}")
    private String ctiHost;

    @Value("${area.agent.client.cti.sip.port}")
    private int ctiPort;

    @Override
    public String call(String from, String to, int maxAnswerSec, int maxRingSec) throws YunhuniApiException {

        String callid = UUIDGenerator.uuid();
        String params = "to=%s&from=%s&maxAnswerSec=%d&maxRingSec=%d&callid=%s";
        params = String.format(params, to, from, maxAnswerSec, maxRingSec, callid);

        try {
            //找到合适的区域代理

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("发送SYS_CALL指令到区域:{}", rpcrequest);
                    }

                    /*发送给区域的请求次数计数*/
                    if (cs != null) cs.getSendAreaNodeRequestCount().incrementAndGet();

                    tzb.doCallZB(to,rpcrequest);

                    rpcCaller.invoke(sessionContext, rpcrequest);

                    /*呼叫API调用次数计数*/
                    if(cs!=null)cs.getSendAreaNodeSysCallCount().incrementAndGet();
                } catch (Exception e) {
                    throw new InvokeCallException(e);
                }
            return callid;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String duoCallback(String ip,String appId, DuoCallbackDTO dto) throws YunhuniApiException {
        String duocCallId = UUIDGenerator.uuid();
        String to1 = dto.getTo1();
        String to2 = dto.getTo2();
        if(apiGwRedBlankNumService.isRedOrBlankNum(to1) || apiGwRedBlankNumService.isRedOrBlankNum(to2)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);

        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(app.getIsVoiceCallback() != 1){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough("duo_call", app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }


        //TODO 获取线路IP和端口
        Map<String, Object> params = new HashMap<>();
        params.put("from1_uri", dto.getFrom1()+"@"+ctiHost+":"+ctiPort);
        params.put("to1_uri", dto.getTo1()+"@"+ctiHost+":"+ctiPort);
        params.put("from2_uri", dto.getFrom2()+"@"+ctiHost+":"+ctiPort);
        params.put("to2_uri",dto.getTo2()+"@"+ctiHost+":"+ctiPort);
        params.put("max_connect_seconds",dto.getMax_call_duration());
        params.put("max_ring_seconds",dto.getMax_dial_duration());
        params.put("ring_play_file",dto.getRing_tone());
        params.put("ring_play_mode",dto.getRing_tone_mode());
        params.put("user_data",duocCallId);
        //录音
        if(dto.getRecording()){
            //TODO 录音文件名称
            params.put("record_file ",duocCallId);
            params.put("record_mode",dto.getRecord_mode());
            params.put("record_format ",1);
        }

        try {
            //找到合适的区域代理
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK, params);
            try {
                rpcCaller.invoke(sessionContext, rpcrequest);
                //将数据存到redis
                BusinessState cache = new BusinessState(app.getTenant().getId(),app.getId(),duocCallId,"duo_call", dto.getUser_data());
                businessStateService.save(cache);
            } catch (Exception e) {
                logger.error("消息发送到区域失败:{}", rpcrequest);
                throw new InvokeCallException(e);
            }
            return duocCallId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String notifyCall(String ip, String appId, NotifyCallDTO dto) throws YunhuniApiException{
        String callId = UUIDGenerator.uuid();
        String to = dto.getTo();
        if(apiGwRedBlankNumService.isRedOrBlankNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(app.getIsVoiceCallback() != 1){
            throw new AppServiceInvalidException();
        }
        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough("notify_call", app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO 获取线路IP和端口
        Map<String, Object> params = new HashMap<>();
        params.put("from_uri", dto.getFrom()+"@"+ctiHost+":"+ctiPort);
        params.put("to_uri", dto.getTo()+"@"+ctiHost+":"+ctiPort);
        params.put("play_content",JSONUtil.objectToJson(dto.getFiles()));
        params.put("play_repeat",dto.getRepeat());
        params.put("max_ring_seconds",dto.getMax_dial_duration());
        params.put("user_data",callId);

        try {
            //找到合适的区域代理
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_NOTIFY_CALL, params);
            rpcCaller.invoke(sessionContext, rpcrequest);
            //将数据存到redis
            BusinessState cache = new BusinessState(app.getTenant().getId(),app.getId(),callId,"notify_call", dto.getUser_data());
            businessStateService.save(cache);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String captchaCall(String ip, String appId, CaptchaCallDTO dto) throws YunhuniApiException{
        String callId = UUIDGenerator.uuid();
        String to = dto.getTo();
        if(apiGwRedBlankNumService.isRedOrBlankNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(app.getIsVoiceValidate() != 1){
            throw new AppServiceInvalidException();
        }
        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough("captcha_call", app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO 获取线路IP和端口
        //TODO 待定
        Map<String, Object> params = new HashMap<>();
        params.put("from_uri", dto.getFrom()+"@"+ctiHost+":"+ctiPort);
        params.put("to_uri", dto.getTo()+"@"+ctiHost+":"+ctiPort);
        params.put("max_ring_seconds",dto.getMax_dial_duration());
        params.put("valid_keys",dto.getVerify_code());
        params.put("user_data",callId);

        try {
            //找到合适的区域代理
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_CAPTCHA_CALL, params);
            rpcCaller.invoke(sessionContext, rpcrequest);
            //将数据存到redis
            BusinessState cache = new BusinessState(app.getTenant().getId(),app.getId(),callId,"captcha_call", dto.getUser_data());
            businessStateService.save(cache);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

}
