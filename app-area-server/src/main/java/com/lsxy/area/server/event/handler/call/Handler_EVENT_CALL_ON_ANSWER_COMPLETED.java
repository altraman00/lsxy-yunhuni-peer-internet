package com.lsxy.area.server.event.handler.call;

import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.event.EventHandler;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.event.Constants;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.framework.rpc.exceptions.InvalidParamException;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.collections.MapUtils;
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
public class Handler_EVENT_CALL_ON_ANSWER_COMPLETED extends EventHandler{

    private static final Logger logger = LoggerFactory.getLogger(Handler_EVENT_CALL_ON_ANSWER_COMPLETED.class);

    @Autowired
    private AppService appService;

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionService ivrActionService;

    @Override
    public String getEventName() {
        return Constants.EVENT_SYS_CALL_ON_ANSWER_COMPLETED;
    }

    /**
     * 接收应答调用成功事件后需要调用ivr询问开发者下一步干嘛
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
        String error = (String)params.get("error");
        if(StringUtils.isNotBlank(error)){
            logger.info("应答出错:{}",error);
            return res;
        }
        String call_id = (String)params.get("user_data");
        if(call_id == null){
            throw new InvalidParamException("call_id=null");
        }
        //删除等待应答标记
        businessStateService.deleteInnerField(call_id,IVRActionService.IVR_ANSWER_WAITTING_FIELD);
        ivrActionService.doAction(call_id,null);
        return res;
    }


}
