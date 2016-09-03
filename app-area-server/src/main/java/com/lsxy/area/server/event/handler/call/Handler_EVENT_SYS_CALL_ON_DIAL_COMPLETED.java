package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.ivr.act.IVRActionUtil;
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
public class Handler_EVENT_SYS_CALL_ON_DIAL_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_DIAL_COMPLETED.class);

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private AppService appService;

    @Autowired
    private ConfService confService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionUtil ivrActionUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_DIAL_COMPLETED;
    }

    /**
     * 拨号结束事件处理
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
        //错误信息。如果拨号失败，该参数记录错误信息。如果拨号成功的被接听，该参数的值是 null。
        String error = (String)request.getParamMap().get("error");
        if(StringUtils.isNotBlank(error)){
            logger.error(error);
            return res;
        }
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
        Map<String,Object> businessData = state.getBusinessData();
        if("sys_conf".equals(state.getType())){//该呼叫是通过(会议邀请呼叫)发起需要将呼叫加入会议
            String conf_id = null;
            if(businessData!=null){
                conf_id = (String)businessData.get("conf_id");
            }
            if(conf_id == null){
                logger.info("将呼叫加入到会议失败conf_is为null");
                return res;
            }
            try {
                confService.confEnter(call_id,conf_id);
            } catch (Throwable e) {
                logger.error("将呼叫加入到会议失败",e);
            }
        }else if("ivr_call".equals(state.getType())){//通过ivr呼出api 发起的呼叫
            App app = appService.findById(state.getAppId());
            //发送拨号结束通知
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .put("event","ivr.dial_end")
                    .put("id",call_id)
                    .put("begin_time",request.getParamMap().get("begin_time"))
                    .put("end_time",request.getParamMap().get("end_time"))
                    .put("user_data",state.getUserdata())
                    .build();
            notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
            ivrActionUtil.doAction(call_id);
        }else if("ivr_incoming".equals(state.getType())){//通过ivr呼入 发起的呼叫
            App app = appService.findById(state.getAppId());
            //发送拨号结束通知
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .put("event","ivr.dial_end")
                    .put("id",call_id)
                    .put("begin_time",request.getParamMap().get("begin_time"))
                    .put("end_time",request.getParamMap().get("end_time"))
                    .put("user_data",state.getUserdata())
                    .build();
            notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        }
        return res;
    }


}
