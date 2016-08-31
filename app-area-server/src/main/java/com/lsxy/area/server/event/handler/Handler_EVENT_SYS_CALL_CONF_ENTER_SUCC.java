package com.lsxy.area.server.event.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CALL_CONF_ENTER_SUCC extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_CONF_ENTER_SUCC.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_CONF_ENTER_SUCC;
    }

    /**
     * 接收到加入会议成功事件，需要向开发者发送通知
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
        String call_id = (String)request.getParamMap().get("user_data");
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
        String callbackUrl = null;
        String conf_id = null;
        if(businessData!=null){
            callbackUrl = (String)businessData.get("callback_url");
            conf_id = (String)businessData.get("conf_id");
        }
        if(StringUtils.isBlank(conf_id)){
            logger.info("没有找到对应的会议信息callid={},confid={}",call_id,conf_id);
            return res;
        }
        if(StringUtils.isBlank(appId)){
            logger.info("没有找到对应的app信息appId={}",appId);
            return res;
        }
        App app = appService.findById(state.getAppId());
        if(app == null){
            logger.info("没有找到对应的app信息appId={}",appId);
            return res;
        }
        if(StringUtils.isBlank(callbackUrl)){
            callbackUrl = app.getUrl();
        }
        if(StringUtils.isBlank(callbackUrl)){
            logger.info("没有找到对应的http通知地址",appId);
            return res;
        }
        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议加入通知给开发者");
        }
        String notify_url = callbackUrl+"/joinconf";
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .put("user_data",user_data)
                .put("appid",appId)
                .put("join_time",System.currentTimeMillis())
                .put("call_id",call_id)
                .put("confid",conf_id).build();
        notifyCallbackUtil.postNotify(notify_url,notify_data,3);
        if(logger.isDebugEnabled()){
            logger.debug("会议加入通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        return res;
    }


}
