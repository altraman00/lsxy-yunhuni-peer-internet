package com.lsxy.area.server.service.callcenter.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.callcenter.AgentIdCallReference;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.lsxy.framework.core.exceptions.api.ApiReturnCodeEnum.CallNotExists;

/**
 * Created by Administrator on 2017/1/9.
 */
@Service
@Component
public class ConversationOps implements com.lsxy.area.api.callcenter.ConversationOps{

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
    private AgentIdCallReference agentIdCallReference;

    @Override
    public boolean dismiss(String ip, String appId, String conversationId) throws YunhuniApiException{
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
        return conversationService.dismiss(appId, conversationId);
    }

    @Override
    public boolean setVoiceMode(String ip, String appId, String conversationId, String agentId, Integer voiceMode) throws YunhuniApiException {
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
        String callId = agentIdCallReference.get(agentId);
        if(callId == null){
            throw new CallNotExistsException();
        }
        return conversationService.setVoiceMode(conversationId,callId,voiceMode);
    }

}
