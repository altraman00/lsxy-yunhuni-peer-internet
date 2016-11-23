package com.lsxy.area.server.service.callcenter;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.call.center.api.model.EnQueueResult;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class DeQueueServiceImpl implements DeQueueService {

    private static final Logger logger = LoggerFactory.getLogger(DeQueueServiceImpl.class);

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    /**
     * 创建交谈，然后呼叫坐席。
     * 交谈创建成功事件邀请排队的客户到交谈
     * 开始呼叫事件中，将坐席加入交谈
     * 坐席拨号完成事件中，播放工号
     * 客户和坐席开始交谈
     * @param tenantId
     * @param appId
     * @param callId
     * @param queueId
     * @param result
     * @throws Exception
     */
    @Override
    public void success(String tenantId, String appId, String callId,
                        String queueId, EnQueueResult result) throws Exception{
        if(logger.isDebugEnabled()){
            logger.debug("排队成功，tenantId={},appId={},callId={},queueId={},result=",
                    tenantId,appId,callId,queueId,result);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || state.getClosed()){
            logger.info("会话已关闭callid={}",callId);
            //抛异常后呼叫中心微服务会回滚坐席状态
            throw new IllegalStateException("会话已关闭");
        }
        Map<String,Object> businessData = state.getBusinessData();
        if(businessData == null){
            businessData = new HashMap<>();
            state.setBusinessData(businessData);
        }
        String to = result.getExtension().getTelenum();
        String conversation = conversationService.create(state.getId(),
                (String)businessData.get(ConversationService.CALLCENTER_ID_FIELD),state.getAppId(),null,false,true,null);
        conversationService.inviteAgent(appId,conversation,null,null,to,null,null,null,null);
    }
    @Override
    public void timeout(String tenantId, String appId, String callId) {
        if(logger.isDebugEnabled()){
            logger.debug("排队超时,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || state.getClosed()){
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        App app = appService.findById(appId);
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .putIfNotEmpty("event","callcenter.enqueue.timeout")
                .putIfNotEmpty("id",callId)
                .putIfNotEmpty("user_data",state.getUserdata())
                .build();
        if(notifyCallbackUtil.postNotifySync(app.getUrl(),notify_data,null,3)){
            ivrActionService.doAction(callId);
        }
    }

    @Override
    public void fail(String tenantId, String appId, String callId, String reason) {
        if(logger.isDebugEnabled()){
            logger.debug("排队失败,tenantId={},appId={},callId={}",tenantId,appId,callId);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null || state.getClosed()){
            logger.info("会话已关闭callid={}",callId);
            return;
        }
        App app = appService.findById(appId);
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .putIfNotEmpty("event","callcenter.enqueue.fail")
                .putIfNotEmpty("id",callId)
                .putIfNotEmpty("user_data",state.getUserdata())
                .build();
        if(notifyCallbackUtil.postNotifySync(app.getUrl(),notify_data,null,3)){
            ivrActionService.doAction(callId);
        }
    }
}
