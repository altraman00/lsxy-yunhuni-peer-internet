package com.lsxy.area.server.event.handler.conf;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.agentserver.RecordCompletedEvent;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/30.
 */
@Component
public class Handler_EVENT_SYS_CONF_ON_RECORD_COMPLETED extends EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_RECORD_COMPLETED.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private MQService mqService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_RECORD_COMPLETED;
    }

    /**
     * 会议录音结束的事件，需要向开发者发送会议录音结束通知
     * @param request
     * @param session
     * @return
     */
    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            throw new InvalidParamException("request params is null");
        }
        String conf_id = (String)params.get("user_data");
        if(StringUtils.isBlank(conf_id)){
            throw new InvalidParamException("conf_id is null");
        }
        BusinessState state = businessStateService.get(conf_id);
        if(state == null){
            throw new InvalidParamException("businessstate is null,call_id={}",conf_id);
        }

        if(logger.isDebugEnabled()){
            logger.info("conf_id={},state={}",conf_id,state);
        }

        try{
            mqService.publish(new RecordCompletedEvent(state.getTenantId(),state.getAppId(),state.getSubaccountId(),state.getAreaId(),state.getId(),
                    ProductCode.changeApiCmdToProductCode(state.getType()).name(),(String)params.get("record_file"),
                    Long.parseLong((String)params.get("begin_time")),Long.parseLong((String)params.get("end_time"))
            ));
        }catch (Throwable t){
            logger.info("发布RecordCompletedEvent失败,state={},params={}",state,params);
            logger.error("发布RecordCompletedEvent失败",t);
        }

        if(BusinessState.TYPE_CC_CONVERSATION.equals(state.getType())){
            conversation(state,params,conf_id);
        }else{
            conf(state,params,conf_id);
        }
        return res;
    }

    private void conversation(BusinessState state, Map<String, Object> params, String conversation_id) {
        //TODO
    }

    private void conf(BusinessState state,Map<String,Object> params,String conf_id){
        String user_data = state.getUserdata();

        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议录音结束通知给开发者");
        }
        Long begin_time = null;
        Long end_time = null;
        if(params.get("begin_time") != null){
            begin_time = (Long.parseLong(params.get("begin_time").toString())) * 1000;
        }
        if(params.get("end_time") != null){
            end_time = (Long.parseLong(params.get("end_time").toString())) * 1000;
        }
        if(StringUtils.isNotBlank(state.getCallBackUrl())){
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","conf.record_end")
                    .putIfNotEmpty("id",conf_id)
                    .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                    .putIfNotEmpty("begin_time",begin_time)
                    .putIfNotEmpty("end_time",end_time)
                    .putIfNotEmpty("user_data",user_data)
                    .build();

            notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,3);
        }
        if(logger.isDebugEnabled()){
            logger.debug("会议录音结束通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
    }
}
