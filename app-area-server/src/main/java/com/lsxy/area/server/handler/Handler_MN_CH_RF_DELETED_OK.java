package com.lsxy.area.server.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/9/12.
 */
@Component
public class Handler_MN_CH_RF_DELETED_OK extends RpcRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_RF_DELETED_OK.class);

    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_RF_DELETED_OK;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应MN_CH_RF_DELETED_OK:{}",request);
        }
        String jsonList = request.getBodyAsString();
        List<Map> list = JSON.parseArray(jsonList, Map.class);
        List list1 = new ArrayList();
        for(int i=0;i<list.size();i++) {
            Map map = list.get(i);
            boolean flag = (boolean) map.get("flag");
            if (flag) {
                list1.add(map.get("id"));
            } else {
                //失败不用更新
                logger.error("删除录音文件失败记录,"+ map);
            }
        }
        try {
            voiceFileRecordService.batchUpdateAADelete(list1, VoiceFilePlay.DELETED_SUCCESS);
            if(logger.isDebugEnabled()) {
                logger.debug("删除录音文件，更新成功记录:"+list1);
            }
        } catch (Exception e) {
            logger.error("删除放音文件，更新失败记录:"+list1+",异常{}" ,e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("响应MN_CH_RF_DELETED_OK操作结束:{}", request);
        }
        return null;
    }

}
