package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.callcenter.AgentIdCallReference;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationCallVoiceModeReference;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * Created by liuws on 2017/1/9.
 */
@Service
@Component
public class AgentOps implements com.lsxy.call.center.api.service.AgentOps {

    public static final Logger logger = LoggerFactory.getLogger(AgentOps.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    private AgentIdCallReference agentIdCallReference;

    @Autowired
    private IVRActionService ivrActionService;

    @Reference(lazy = true,check = false,timeout = 30000)
    private EnQueueService enQueueService;

    @Autowired
    private DeQueueService deQueueService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private ConversationCallVoiceModeReference conversationCallVoiceModeReference;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationService callCenterConversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterQueueService callCenterQueueService;


    @Override
    public void reject(String ip, String appId, String name, String queueId, String userData) throws YunhuniApiException {
        if(StringUtils.isBlank(name)){
            throw new RequestIllegalArgumentException();
        }
        if(StringUtils.isBlank(queueId)){
            throw new RequestIllegalArgumentException();
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException();
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }

        CallCenterQueue callCenterQueue = callCenterQueueService.findById(queueId);
        if(callCenterQueue == null){
            logger.warn("找不到对应的排队记录id={}",queueId);
            throw new RequestIllegalArgumentException();
        }
        String agentCallId = callCenterQueue.getAgentCallId();

        BusinessState state = businessStateService.get(agentCallId);

        if(state == null || (state.getClosed()!=null && state.getClosed())){
            throw new CallNotExistsException();
        }

        if(state.getResId() == null){
            throw new SystemBusyException();
        }

        if(state.getBusinessData().get(BusinessState.RINGING_TAG) == null){
            //TODO 不是正在振铃
            throw new SystemBusyException();
        }

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",state.getResId())
                .putIfNotEmpty("user_data",state.getId())
                .put("areaId",state.getAreaId())
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            throw new InvokeCallException();
        }
    }

    @Override
    public boolean callOut(String ip, String appId, String name, String from, String to, Integer maxDialSeconds, Integer maxAnswerSeconds) throws YunhuniApiException {

        return false;
    }

}
