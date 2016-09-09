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
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    @Autowired
    private CalCostService calCostService;


    @Override
    public String ivrCall(String ip, String appId, String from, String to,
                          Integer maxDialDuration, Integer maxCallDuration, String userData) throws YunhuniApiException {
        if(apiGwRedBlankNumService.isRedNum(to)){
            throw new NumberNotAllowToCallException();
        }
        App app = appService.findById(appId);
        String tenantId = app.getTenant().getId();
        if(app.getStatus() != app.STATUS_ONLINE){
            throw new AppOffLineException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        if(app.getIsIvrService() == null || app.getIsIvrService() != 1){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.ivr_call.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        String callId = UUIDGenerator.uuid();
        //TODO
        String oneTelnumber = appService.findOneAvailableTelnumber(app);
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",to+"@"+lineGateway.getIp()+":"+lineGateway.getPort())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_answer_seconds",maxCallDuration)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .put("appid ",app.getId())
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
                                    .setAppId(appId)
                                    .setId(callId)
                                    .setType("ivr_call")
                                    .setAreaId(app.getArea().getId())
                                    .setLineGatewayId(lineGateway.getId())
                                    .setBusinessData(new MapBuilder<String,Object>()
                                            .put("begin_time",System.currentTimeMillis())
                                            .put("from",from)
                                            .put("to",to)
                                            .build())
                                    .build();
        businessStateService.save(callstate);
        return callId;
    }
}
