package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.IVRService;
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
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class IVRServiceImpl implements IVRService {

    private static final Logger logger = LoggerFactory.getLogger(IVRServiceImpl.class);

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
    public String ivrCall(String ip, String appId, String from, String to,
                          Integer maxDialDuration, Integer maxCallDuration, String userData) throws YunhuniApiException {
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

        if(app.getIsIvrService() == null || app.getIsIvrService() != 1){
            throw new AppServiceInvalidException();
        }

        BigDecimal balance = calBillingService.getBalance(app.getTenant().getId());
        //TODO 判断余额是否充足
        if(balance.compareTo(new BigDecimal(0)) != 1){
            throw new BalanceNotEnoughException();
        }

        String callId = UUIDGenerator.uuid();
        //TODO
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .put("to_uri",to+"@"+lineGateway.getIp()+":"+lineGateway.getPort())
                .put("from_uri",oneTelnumber)
                .put("max_answer_seconds",maxCallDuration)
                .put("max_ring_seconds",maxDialDuration)
                .put("user_data",callId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            throw new InvokeCallException(e);
        }
        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState(tenantId,app.getId(),callId,"ivr_call",null
                ,new MapBuilder<String,Object>()
                .put("begin_time",System.currentTimeMillis())
                .put("from",from)
                .put("to",to)
                .build());
        businessStateService.save(callstate);
        return callId;
    }
}
