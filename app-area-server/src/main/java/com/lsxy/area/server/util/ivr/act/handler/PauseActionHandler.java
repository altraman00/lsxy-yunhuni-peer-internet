package com.lsxy.area.server.util.ivr.act.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.agentserver.IVRPauseActionEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/9/2.
 */
@Component
public class PauseActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private MQService mqService;

    @Override
    public String getAction() {
        return "pause";
    }

    @Override
    public boolean handle(String callId, Element root) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr动作，callId={},act={}",callId,getAction());
        }
        String nextUrl = "";
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr[{}]动作",getAction());
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("没有找到call_id={}的state",callId);
            return false;
        }
        Integer duration = null;
        Attribute attr = root.attribute("duration");
        if(attr != null){
            String duration_str = root.attribute("duration").getValue();
            if(StringUtils.isNotBlank(duration_str) && StringUtils.isNumeric(duration_str)){
                duration = Integer.parseInt(duration_str);
            }
        }
        mqService.publish(new IVRPauseActionEvent(callId,duration));
        Map<String,Object> businessData = state.getBusinessData();
        if(businessData == null){
            businessData = new HashMap<>();
        }
        businessData.put("next",nextUrl);
        state.setBusinessData(businessData);
        businessStateService.save(state);
        return true;
    }
}
