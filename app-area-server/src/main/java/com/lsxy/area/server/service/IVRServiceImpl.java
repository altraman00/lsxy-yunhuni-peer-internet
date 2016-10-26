package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.IVRService;
import com.lsxy.area.api.exceptions.*;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.TenantServiceSwitch;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.utils.MapBuilder;
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
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    private boolean isEnableIVRService(String tenantId,String appId){
        try {
            TenantServiceSwitch serviceSwitch = tenantServiceSwitchService.findOneByTenant(tenantId);
            if(serviceSwitch != null && (serviceSwitch.getIsIvrService() == null || serviceSwitch.getIsIvrService() != 1)){
                return false;
            }
            App app = appService.findById(appId);
            if(app.getIsIvrService() == null || app.getIsIvrService() != 1){
                return false;
            }
        } catch (Throwable e) {
            logger.error("判断是否开启service失败",e);
            return false;
        }
        return true;
    }

    @Override
    public String ivrCall(String ip, String appId, String from, String to,
                          Integer maxDialDuration, Integer maxCallDuration, String userData) throws YunhuniApiException {
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

        if(!isEnableIVRService(tenantId,appId)){
            throw new AppServiceInvalidException();
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.ivr_call.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO
        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(app, to);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber().getTelNumber();
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);

        VoiceIvr voiceIvr = new VoiceIvr();
        voiceIvr.setFromNum(oneTelnumber);
        voiceIvr.setToNum(to);
        voiceIvr.setStartTime(new Date());
        voiceIvr.setIvrType(VoiceIvr.IVR_TYPE_CALL);
        voiceIvr = voiceIvrService.save(voiceIvr);
        String callId = voiceIvr.getId();

        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(oneTelnumber);
        callSession.setToNum(to+"@"+lineGateway.getSipProviderIp());
        callSession.setApp(app);
        callSession.setTenant(app.getTenant());
        callSession.setRelevanceId(callId);
        callSession.setType(CallSession.TYPE_VOICE_IVR);
        callSession.setResId(null);
        callSession = callSessionService.save(callSession);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",to+"@"+lineGateway.getSipProviderIp())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_answer_seconds",maxCallDuration)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .putIfNotEmpty("areaId ",areaId)
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
                                    .setAreaId(areaId)
                                    .setLineGatewayId(lineGateway.getId())
                                    .setBusinessData(new MapBuilder<String,Object>()
                                            .putIfNotEmpty("from",from)
                                            .putIfNotEmpty("to",to)
                                            .putIfNotEmpty("sessionid",callSession.getId())
                                            .build())
                                    .build();
        businessStateService.save(callstate);
        return callId;
    }
}
