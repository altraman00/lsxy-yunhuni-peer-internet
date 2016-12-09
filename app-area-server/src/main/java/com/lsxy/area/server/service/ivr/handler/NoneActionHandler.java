package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * none指令处理器
 * Created by liuws on 2016/9/2.
 */
@Component
@Deprecated
public class NoneActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Override
    public String getAction() {
        return "none";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT_FIELD,next);
        return true;
    }
}
