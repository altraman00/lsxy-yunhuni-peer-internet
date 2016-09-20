package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
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
public class Handler_EVENT_SYS_CALL_ON_CONF_COMPLETED extends EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_CONF_COMPLETED.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private ConfService confService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_CONF_COMPLETED;
    }

    /**
     * 呼叫加入会议结束的事件，需要向开发者发送离开会议通知
     * @param request
     * @param session
     * @return
     */
    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        if(MapUtils.isEmpty(params)){
            logger.error("request params is null");
            return res;
        }
        String call_id = (String)params.get("user_data");
        if(StringUtils.isBlank(call_id)){
            logger.info("call_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(call_id);
        if(state == null){
            logger.info("businessstate is null");
            return res;
        }

        if(logger.isDebugEnabled()){
            logger.info("call_id={},state={}",call_id,state);
        }

        String appId = state.getAppId();
        String user_data = state.getUserdata();
        Map<String,Object> businessData = state.getBusinessData();
        String conf_id = null;
        if(businessData!=null){
            conf_id = (String)businessData.get("conf_id");
        }
        if(StringUtils.isBlank(conf_id)){
            logger.info("没有找到对应的会议信息callid={},confid={}",call_id,conf_id);
            return res;
        }
        //会议成员递减
        confService.decrPart(conf_id,call_id);
        if(StringUtils.isBlank(appId)){
            logger.info("没有找到对应的app信息appId={}",appId);
            return res;
        }
        App app = appService.findById(state.getAppId());
        if(app == null){
            logger.info("没有找到对应的app信息appId={}",appId);
            return res;
        }
        if(StringUtils.isBlank(app.getUrl())){
            logger.info("没有找到appId={}的回调地址",appId);
            return res;
        }
        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送离开会议通知给开发者");
        }
        Long begin_time = null;
        Long end_time = null;
        if(params.get("begin_time") != null){
            begin_time = ((long)params.get("begin_time")) * 1000;
        }
        if(params.get("end_time") != null){
            end_time = ((long)params.get("end_time")) * 1000;
        }
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .putIfNotEmpty("event","conf.quit")
                .putIfNotEmpty("id",conf_id)
                .putIfNotEmpty("join_time",begin_time)
                .putIfNotEmpty("quit_time",end_time)
                .putIfNotEmpty("call_id",call_id)
                .putIfNotEmpty("part_uri",null)
                .putIfNotEmpty("user_data",user_data)
                .build();
        notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        if(logger.isDebugEnabled()){
            logger.debug("离开会议通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        return res;
    }
}
