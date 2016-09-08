package com.lsxy.area.server.event.handler.conf;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
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
public class Handler_EVENT_SYS_CONF_ON_FAIL extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_FAIL.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_FAIL;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        String conf_id = (String)request.getParamMap().get("user_data");
        if(StringUtils.isBlank(conf_id)){
            logger.info("conf_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(conf_id);
        if(state == null){
            logger.info("businessstate is null");
            return res;
        }
        if(logger.isDebugEnabled()){
            logger.info("confi_id={},state={}",conf_id,state);
        }

        String appId = state.getAppId();
        String user_data = state.getUserdata();

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
            logger.debug("开始发送会议创建失败通知给开发者");
        }
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .put("event","conf.create.fail")
                .put("id",conf_id)
                .put("user_data",user_data)
                .build();
        notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        if(logger.isDebugEnabled()){
            logger.debug("会议创建失败通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        return res;
    }
}
