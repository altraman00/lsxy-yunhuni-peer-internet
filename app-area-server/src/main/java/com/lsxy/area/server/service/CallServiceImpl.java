package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.area.api.CallCacheVO;
import com.lsxy.area.api.CallService;
import com.lsxy.area.api.DuoCallbackVO;
import com.lsxy.area.api.NotifyCallVO;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil;
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
import java.util.ArrayList;
import java.util.List;
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
    private ServerSessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private BillingService billingService;

    @Autowired
    RedisCacheService redisCacheService;

    @Override
    public String call(String from, String to, int maxAnswerSec, int maxRingSec) throws YunhuniApiException {

        String callid = UUIDGenerator.uuid();
        String params = "to=%s&from=%s&maxAnswerSec=%d&maxRingSec=%d&callid=%s";
        params = String.format(params, to, from, maxAnswerSec, maxRingSec, callid);

        try {
            //找到合适的区域代理
            Session session = sessionContext.getRightSession();
            if (session != null) {

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("发送SYS_CALL指令到区域:{}", rpcrequest);
                    }

                    /*发送给区域的请求次数计数*/
                    if (cs != null) cs.getSendAreaNodeRequestCount().incrementAndGet();

                    tzb.doCallZB(to,rpcrequest);

                    rpcCaller.invoke(session, rpcrequest);

                    /*呼叫API调用次数计数*/
                    if(cs!=null)cs.getSendAreaNodeSysCallCount().incrementAndGet();
                } catch (Exception e) {
                    logger.error("消息发送到区域失败:{}", rpcrequest);
                    throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
                }
            } else {
                logger.error("没有找到合适的区域代理处理该请求:sys.call=>{}", params);
                throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.call=>" + params);
            }
            return callid;
        }catch(RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
    }

    @Override
    public String duoCallback(String ip,String appId, DuoCallbackVO duoCallbackVO) throws YunhuniApiException {
        String callId = UUIDGenerator.uuid();
        String to1 = duoCallbackVO.getTo1();
        String to2 = duoCallbackVO.getTo2();
        if(apiGwRedBlankNumService.isRedOrBlankNum(to1) || apiGwRedBlankNumService.isRedOrBlankNum(to2)){
            throw new NumberNotAllowToCallException("不能呼叫该号码");
        }
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("id不在白名单");
            }
        }
        if(app.getIsVoiceCallback() != 1){
            throw new AppServiceInvalidException("app没开通所需的服务");
        }
        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException("余额不足");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(duoCallbackVO, Map.class);
        map.put("callId",callId);
        String params = mapToString(map);
        try {
            //找到合适的区域代理
            Session session = sessionContext.getRightSession();
            if (session != null) {
                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK, params);
                try {
                    rpcCaller.invoke(session, rpcrequest);
                    //将数据存到redis
                    redisCacheService.set("call_"+callId,JSONUtil.objectToJson(new CallCacheVO(callId,"duo_call",null,duoCallbackVO.getUser_data())),5 * 60 * 60);
                } catch (Exception e) {
                    logger.error("消息发送到区域失败:{}", rpcrequest);
                    throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
                }
            } else {
                logger.error("没有找到合适的区域代理处理该请求:sys.call=>{}", params);
                throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.call=>" + params);
            }
            return callId;
        }catch(RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
    }

    @Override
    public String notifyCall(String ip, String appId, NotifyCallVO notifyCallVO) throws YunhuniApiException{
        String callId = UUIDGenerator.uuid();
        String to1 = notifyCallVO.getTo();
        if(apiGwRedBlankNumService.isRedOrBlankNum(to1)){
            throw new NumberNotAllowToCallException("不能呼叫该号码");
        }
        App app = appService.findById(appId);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList.trim())){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException("id不在白名单");
            }
        }
        if(app.getIsVoiceCallback() != 1){
            throw new AppServiceInvalidException("app没开通所需的服务");
        }
        BigDecimal balance = billingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException("余额不足");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(notifyCallVO, Map.class);
        map.put("callId",callId);
        String params = mapToString(map);
        try {
            //找到合适的区域代理
            Session session = sessionContext.getRightSession();
            if (session != null) {
                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_NOTIFY_CALL, params);
                try {
                    rpcCaller.invoke(session, rpcrequest);
                    //将数据存到redis
                    redisCacheService.set("call_"+callId,JSONUtil.objectToJson(new CallCacheVO(callId,"notify_call",null,notifyCallVO.getUser_data())),5 * 60 * 60);
                } catch (Exception e) {
                    logger.error("消息发送到区域失败:{}", rpcrequest);
                    throw new InvokeCallException("消息发送到区域失败:" + rpcrequest);
                }
            } else {
                logger.error("没有找到合适的区域代理处理该请求:sys.call=>{}", params);
                throw new InvokeCallException("没有找到合适的区域代理处理该请求:sys.call=>" + params);
            }
            return callId;
        }catch(RightSessionNotFoundExcepiton ex){
            throw new InvokeCallException(ex.getMessage());
        }
    }


    public String mapToString(Map<String,Object> params){
            List<String> keys = new ArrayList<>(params.keySet());
            String result = "";
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                String value = params.get(key)+"";
                if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                    result = result + key + "=" + value;
                } else {
                    result = result + key + "=" + value + "&";
                }
            }
            return result;
    }

}
