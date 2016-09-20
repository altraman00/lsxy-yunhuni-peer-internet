package com.lsxy.area.server.service.act.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuws on 2016/9/2.
 */
@Component
public class ReceivedtmfActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Override
    public String getAction() {
        return "get";
    }

    @Override
    public boolean handle(String callId, Element root,String next) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr动作，callId={},act={}",callId,getAction());
        }

        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("没有找到call_id={}的state",callId);
            return false;
        }

        String valid_keys = root.attributeValue("valid_keys");
        String max_keys = root.attributeValue("max_keys");
        String finish_keys = root.attributeValue("finish_keys");
        String first_key_timeout = root.attributeValue("first_key_timeout");
        String continues_keys_timeout = root.attributeValue("continues_keys_timeout");
        String play_repeat = root.attributeValue("play_repeat");
        String if_break_on_key = root.attributeValue("if_break_on_key");
        List<String> plays = getPlay(root);

        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr[{}]动作，valid_keys={},max_keys={},finish_keys={}",
                    getAction(),valid_keys,max_keys,finish_keys);
        }

        Map<String,Object> businessData = state.getBusinessData();
        String res_id = state.getResId();

        try {
            plays = playFileUtil.convertArray(state.getTenantId(),state.getAppId(),plays);
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("valid_keys",valid_keys)
                    .putIfNotEmpty("max_keys",max_keys)
                    .putIfNotEmpty("finish_keys",finish_keys)
                    .putIfNotEmpty("first_key_timeout",first_key_timeout)
                    .putIfNotEmpty("continues_keys_timeout",continues_keys_timeout)
                    .putIfNotEmpty("play_content", JSONUtil2.objectToJson(new Object[][]{new Object[]{StringUtils.join(plays,"|"),7,""}}))
                    .putIfNotEmpty("play_repeat",play_repeat)
                    .putIfNotEmpty("breaking_on_key",if_break_on_key)
                    .putIfNotEmpty("user_data",callId)
                    .put("appid",state.getAppId())
                    .build();

            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_RECEIVE_DTMF_START, params);
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Throwable e) {
            logger.error("调用失败",e);
        }
        if(businessData == null){
            businessData = new HashMap<>();
        }
        businessData.put("next",next);
        state.setBusinessData(businessData);
        businessStateService.save(state);
        return true;
    }

    private List<String> getPlay(Element root) {
        List<String> plays = new ArrayList<>();
        try{
            List<Element> playlists = root.elements("playlist");
            if(playlists!=null && playlists.size()>0){
                for (Element playlist : playlists){
                    List<Element> ps = playlist.elements("play");
                    if(ps!=null && ps.size()>0){
                        for(Element p : ps){
                            if(StringUtils.isNotBlank(p.getTextTrim())){
                                plays.add(p.getTextTrim());
                            }
                        }

                    }
                }
            }
            List<Element> ps = root.elements("play");
            if(ps!=null && ps.size()>0){
                for(Element p : ps){
                    if(StringUtils.isNotBlank(p.getTextTrim())){
                        plays.add(p.getTextTrim());
                    }
                }

            }
        }catch (Throwable t){
            logger.info("解析play失败！",t);
        }
        return  plays;
    }
}
