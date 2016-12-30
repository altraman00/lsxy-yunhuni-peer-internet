package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
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
import java.util.List;
import java.util.Map;

/**
 * 收码指令处理器
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

    @Autowired
    private IVRActionService ivrActionService;

    @Override
    public String getAction() {
        return "get";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        String valid_keys = root.attributeValue("valid_keys");
        String max_keys = root.attributeValue("max_keys");
        String finish_keys = root.attributeValue("finish_keys");
        String first_key_timeout = root.attributeValue("first_key_timeout");
        String continues_keys_timeout = root.attributeValue("continues_keys_timeout");
        String play_repeat = root.attributeValue("play_repeat");
        String if_break_on_key = root.attributeValue("if_break_on_key");
        List<String> plays = getPlay(root);

        String res_id = state.getResId();
        businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT_FIELD,next);
        try {
            plays = playFileUtil.convertArray(state.getTenantId(),state.getAppId(),plays);
            String play_content = null;
            if(plays!=null && plays.size()>0){
                play_content = JSONUtil2.objectToJson(new Object[][]{new Object[]{StringUtils.join(plays,"|"),7,""}});
            }
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("valid_keys",valid_keys)
                    .putIfNotEmpty("max_keys",max_keys)
                    .putIfNotEmpty("finish_keys",finish_keys)
                    .putIfNotEmpty("first_key_timeout",first_key_timeout)
                    .putIfNotEmpty("continues_keys_timeout",continues_keys_timeout)
                    .putIfNotEmpty("play_content", play_content)
                    .putIfNotEmpty("play_repeat",play_repeat)
                    .putIfNotEmpty("breaking_on_key",if_break_on_key)
                    .putIfNotEmpty("user_data",callId)
                    .put("areaId",state.getAreaId())
                    .build();

            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_RECEIVE_DTMF_START, params);
            if(!businessStateService.closed(callId)) {
                rpcCaller.invoke(sessionContext, rpcrequest);
            }
        } catch (Throwable e) {
            logger.error("调用失败",e);
            ivrActionService.doAction(callId,new MapBuilder<String,Object>()
                    .putIfNotEmpty("error","receive error")
                    .build());
        }
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
