package com.lsxy.area.server.event.handler.conf;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.ConfUtil;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.session.model.Meeting;
import com.lsxy.yunhuni.api.session.service.MeetingService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

import java.util.Date;
import java.util.Map;

/**
 * Created by liuws on 2016/8/29.
 */
@Component
public class Handler_EVENT_SYS_CONF_ON_RELEASE extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CONF_ON_RELEASE.class);

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private AppService appService;

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private ConfUtil confUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CONF_ON_RELEASE;
    }

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
        String conf_id = (String)params.get("user_data");
        //TODO 会议与会者list
        String member_ids = (String)params.get("member_ids");
        if(StringUtils.isBlank(conf_id)){
            logger.info("conf_id is null");
            return res;
        }
        BusinessState state = businessStateService.get(conf_id);
        if(state == null){
            logger.info("businessstate is null");
            return res;
        }

        businessStateService.delete(conf_id);

        if(logger.isDebugEnabled()){
            logger.info("confi_id={},state={}",conf_id,state);
        }

        String appId = state.getAppId();
        String user_data = state.getUserdata();
        Map<String,Object> businessData = state.getBusinessData();
        Boolean auto_hangup = Boolean.FALSE;
        if(businessData!=null){
            auto_hangup = (Boolean)businessData.get("auto_hangup");
        }
        if(auto_hangup){
            if(logger.isDebugEnabled()){
                logger.debug("开始挂断会议与会方{}",member_ids);
            }
            handupParts(conf_id);
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

        if(StringUtils.isBlank(app.getUrl())){
            logger.info("没有找到appId={}的回调地址",appId);
            return res;
        }
        //开始通知开发者
        if(logger.isDebugEnabled()){
            logger.debug("开始发送会议解散通知给开发者");
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
                .putIfNotEmpty("event","conf.end")
                .putIfNotEmpty("id",conf_id)
                .putIfNotEmpty("begin_time",begin_time)
                .putIfNotEmpty("end_time",end_time)
                .putIfNotEmpty("end_by",null)
                .putIfNotEmpty("record_files",null)
                .putIfNotEmpty("user_data",user_data)
                .build();
        notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        if(logger.isDebugEnabled()){
            logger.debug("会议解散通知发送成功");
        }
        if(logger.isDebugEnabled()){
            logger.debug("处理{}事件完成",getEventName());
        }

        Meeting meeting = meetingService.findById(conf_id);
        if(meeting!=null){
            meeting.setEndTime(new Date());
            meetingService.save(meeting);
        }
        return res;
    }

    private void handupParts(String confId) {
        List<String> parts = confUtil.getParts(confId);
        if(parts!=null && parts.size()>0){
            for (String callId : parts) {
                handup(callId);
            }
        }
    }

    private void handup(String callId){
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("会议结束自动挂断与会方={}失败,state=null",callId);
            return;
        }
        String res_id = state.getResId();
        if(res_id == null){
            logger.info("会议结束自动挂断与会方={}失败,res_id=null",callId);
            return;
        }
        try {
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("user_data",callId)
                    .put("appid",state.getAppId())
                    .build();

            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_DROP, params);
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("会议结束自动挂断与会方={}失败",e);
        }
    }
}
