package com.lsxy.area.server.event.handler.conf;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
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
public class Handler_EVENT_SYS_CONF_ON_START extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_START.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_START;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
        String conf_id = (String)params.get("user_data");
        String res_id = (String)params.get("res_id");
        if(StringUtils.isBlank(conf_id)){
            logger.info("conf_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(conf_id);
        if(state == null){
            logger.info("businessstate is null");
            return res;
        }
        if(res_id!=null){
            state.setResId(res_id);
            businessStateService.save(state);
        }
        if(logger.isDebugEnabled()){
            logger.info("confi_id={},state={}",conf_id,state);
        }
        String appId = state.getAppId();
        String user_data = state.getUserdata();
        Map<String,Object> businessData = state.getBusinessData();

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
            logger.debug("开始发送会议创建成功通知给开发者");
        }
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .put("event","conf.create.succ")
                .put("id",conf_id)
                .put("begin_time",System.currentTimeMillis())
                .put("user_data",user_data)
                .build();
        notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        if(logger.isDebugEnabled()){
            logger.debug("会议创建成功通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }
        ifAutoRecording(businessData,res_id,conf_id);
        return res;
    }

    /**
     * 创建会议是否自动录音
     * @param businessData
     * @param res_id
     * @param conf_id
     */
    private void ifAutoRecording(Map<String,Object> businessData,String res_id,String conf_id){
        if(businessData == null){
            return;
        }
        Object recording = businessData.get("recording");
        if(recording == null || !(Boolean)recording){
            return;
        }
        int times = 0;
        int max_retry = 3;
        boolean success = false;
        Map<String,Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("res_id",res_id)
                .putIfNotEmpty("max_seconds",businessData.get("max_seconds"))
                //TODO 文件名如何定
                .putIfNotEmpty("record_file", UUIDGenerator.uuid())
                .putIfNotEmpty("user_data",conf_id)
                .build();
        do{
            try {
                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CONF_RECORD, params);
                rpcCaller.invoke(sessionContext, rpcrequest);
                success = true;
            } catch (Exception e) {
                logger.error("会议创建自动录音：",e);
                times++;
            }
        }while (!success && times<=max_retry);
    }
}
