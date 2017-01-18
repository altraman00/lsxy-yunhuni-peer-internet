package com.lsxy.area.server.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.mq.events.portal.VoiceFilePlayDeleteEvent;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by zhangxb on 2016/9/12.
 */
@Component
public class Handler_MN_CH_VF_DELETED_OK extends RpcRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_VF_DELETED_OK.class);

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_VF_DELETED_OK;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应MN_CH_VF_DELETED_OK:{}",request);
        }
        String jsonList = request.getBodyAsString();
        Map<String,Object> map = JSON.parseObject(jsonList, Map.class);
        String type = (String)map.get("type");
        boolean flag = (boolean)map.get("flag");
        if(flag){
            if(VoiceFilePlayDeleteEvent.FILE.equals(type)){
                try {
                    voiceFilePlayService.updateDeletedStautsByid((String) map.get("id"), VoiceFilePlay.DELETED_SUCCESS);
                    if(logger.isDebugEnabled()) {
                        logger.debug("删除放音文件，更新成功记录:{}", map);
                    }
                }catch (Exception e){
                    logger.error("删除放音文件，更新失败记录:"+map.toString()+",异常:",e);
                }
            }else if(VoiceFilePlayDeleteEvent.APP.equals(type)){
                try {
                    voiceFilePlayService.updateDeletedStautsByAppId((String) map.get("id"), VoiceFilePlay.DELETED_SUCCESS);
                    if(logger.isDebugEnabled()) {
                        logger.debug("删除放音文件，更新成功记录:{}", map);
                    }
                }catch (Exception e) {
                    logger.error("删除放音文件，更新失败记录:"+map+",异常:",e);
                }
            }else{
                if(logger.isDebugEnabled()){
                    logger.debug("删除放音文件，文件类型出错:{}", map);
                }
            }
        }else{
            //失败不用更新
            if(logger.isDebugEnabled()){
                logger.debug("删除放音文件失败记录,{}",map);
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("响应MN_CH_VF_DELETED_OK操作结束:{}",request);
        }
        return null;
    }
}
