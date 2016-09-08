package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.ivr.act.IVRActionUtil;
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
public class Handler_EVENT_SYS_CALL_ON_RELEASE extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_SYS_CALL_ON_RELEASE.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionUtil ivrActionUtil;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_RELEASE;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理{}事件,{}",getEventName(),request);
        }
        RPCResponse res = null;
        Map<String,Object> params = request.getParamMap();
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
        App app = appService.findById(state.getAppId());
        //发送呼叫结束通知
        Map<String,Object> notify_data = new MapBuilder<String,Object>()
                .put("event","ivr.call_end")
                .put("id",call_id)
                .put("begin_time",params.get("begin_time"))
                .put("answer_time",params.get("answer_time"))
                .put("end_time",params.get("end_time"))
                .put("end_by",params.get("dropped_by"))
                .put("cause",params.get("cause"))
                .put("user_data",state.getUserdata())
                .build();
        notifyCallbackUtil.postNotify(app.getUrl(),notify_data,3);
        return res;
    }
}
