package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.area.api.*;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

import static com.lsxy.yunhuni.api.product.enums.ProductCode.duo_call;

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
    private ServerSessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    BusinessStateService businessStateService;

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
    public String duoCallback(String ip,String appId, DuoCallbackVO duoCallbackVO) throws YunhuniApiException {
        String callId = UUIDGenerator.uuid();
        String to1 = duoCallbackVO.getTo1();
        String to2 = duoCallbackVO.getTo2();
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

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(duoCallbackVO, Map.class);
        map.put("callId",callId);
        try {
            //找到合适的区域代理
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK, map);
            try {
                rpcCaller.invoke(sessionContext, rpcrequest);
                //将数据存到redis
                BusinessState cache = new BusinessState(app.getTenant().getId(),app.getId(),callId,"duo_call",duoCallbackVO.getUser_data());
                businessStateService.save(cache);
            } catch (Exception e) {
                logger.error("消息发送到区域失败:{}", rpcrequest);
                throw new InvokeCallException(e);
            }
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String notifyCall(String ip, String appId, NotifyCallVO notifyCallVO) throws YunhuniApiException{
        String callId = UUIDGenerator.uuid();
        String to1 = notifyCallVO.getTo();
        if(apiGwRedBlankNumService.isRedOrBlankNum(to1)){
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
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(notifyCallVO, Map.class);
        map.put("callId",callId);
        try {
            //找到合适的区域代理
                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_NOTIFY_CALL, map);
                rpcCaller.invoke(sessionContext, rpcrequest);
                //将数据存到redis
                BusinessState cache = new BusinessState(app.getTenant().getId(),app.getId(),callId,"notify_call",notifyCallVO.getUser_data());
                businessStateService.save(cache);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }


}
