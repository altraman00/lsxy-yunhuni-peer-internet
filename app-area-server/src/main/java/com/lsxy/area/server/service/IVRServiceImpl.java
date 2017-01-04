package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.IVRService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.batch.CallCenterBatchInserter;
import com.lsxy.area.server.batch.CallSessionBatchInserter;
import com.lsxy.area.server.batch.VoiceIvrBatchInserter;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    @Autowired
    private CallSessionBatchInserter callSessionBatchInserter;

    @Autowired
    private CallCenterBatchInserter callCenterBatchInserter;

    @Autowired
    private VoiceIvrBatchInserter voiceIvrBatchInserter;

    @Autowired
    private CallCenterStatisticsService callCenterStatisticsService;

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

        if(!appService.enabledService(tenantId,appId, ServiceType.IvrService)){
            throw new AppServiceInvalidException();
        }

        boolean isCallCenter = false;

        if(app.getServiceType().equals(App.PRODUCT_CALL_CENTER)){
            if(!appService.enabledService(tenantId,app.getId(), ServiceType.CallCenter)){
                throw new AppServiceInvalidException();
            }
            isCallCenter = true;
        }else{
            if(!appService.enabledService(tenantId,app.getId(), ServiceType.IvrService)){
                throw new AppServiceInvalidException();
            }
        }

        boolean isAmountEnough = calCostService.isCallTimeRemainOrBalanceEnough(ProductCode.ivr_call.getApiCmd(), app.getTenant().getId());
        if(!isAmountEnough){
            throw new BalanceNotEnoughException();
        }

        //TODO
        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(app,from, to);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber();
        String lineId = selector.getLineId();
        String callId = UUIDGenerator.uuid();

        if(isCallCenter){
            CallCenter callCenter = new CallCenter();
            callCenter.setId(callId);
            callCenter.setTenantId(tenantId);
            callCenter.setAppId(app.getId());
            callCenter.setFromNum(oneTelnumber);
            callCenter.setToNum(to);
            callCenter.setStartTime(new Date());
            callCenter.setType(""+CallCenter.CALL_UP);
            callCenter.setCost(BigDecimal.ZERO);
            callCenterBatchInserter.put(callCenter);
            try{
                callCenterStatisticsService.incrIntoRedis(new CallCenterStatistics.Builder(tenantId,app.getId(),
                        new Date()).setCallOut(1L).build());
            }catch (Throwable t){
                logger.error("incrIntoRedis失败",t);
            }
        }else{
            VoiceIvr voiceIvr = new VoiceIvr();
            voiceIvr.setId(callId);
            voiceIvr.setAppId(app.getId());
            voiceIvr.setTenantId(app.getTenant().getId());
            voiceIvr.setFromNum(oneTelnumber);
            voiceIvr.setToNum(to);
            voiceIvr.setStartTime(new Date());
            voiceIvr.setIvrType(VoiceIvr.IVR_TYPE_CALL);
            voiceIvrBatchInserter.put(voiceIvr);
        }
        CallSession callSession = new CallSession();
        callSession.setId(UUIDGenerator.uuid());
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(oneTelnumber);
        callSession.setToNum(selector.getToUri());
        callSession.setAppId(app.getId());
        callSession.setTenantId(tenantId);
        callSession.setRelevanceId(callId);
        callSession.setResId(null);
        callSession.setType(isCallCenter ? CallSession.TYPE_CALL_CENTER:CallSession.TYPE_VOICE_IVR);
        callSessionBatchInserter.put(callSession);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",selector.getToUri())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_answer_seconds",maxCallDuration)
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
                                    .setAppId(appId)
                                    .setId(callId)
                                    .setType(BusinessState.TYPE_IVR_CALL)
                                    .setCallBackUrl(app.getUrl())
                                    .setAreaId(areaId)
                                    .setLineGatewayId(lineId)
                                    .setUserdata(userData)
                                    .setBusinessData(new MapBuilder<String,String>()
                                            .putIfNotEmpty("from",from)
                                            .putIfNotEmpty("to",to)
                                            .putIfWhere(CallCenterUtil.CALLCENTER_FIELD,isCallCenter,callId)
                                            .putIfWhere(CallCenterUtil.ISCC_FIELD,isCallCenter,CallCenterUtil.ISCC_TRUE)
                                            .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                                            .build())
                                    .build();
        businessStateService.save(callstate);
        return callId;
    }
}
