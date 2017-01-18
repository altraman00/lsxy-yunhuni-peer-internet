package com.lsxy.area.server.mq.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功以后,通知所有区域到OSS下载对应的放音文件
 */
@Component
public class VoiceFilePlayAuditCompletedEventHandler implements MQMessageHandler<VoiceFilePlayAuditCompletedEvent>{

    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayAuditCompletedEventHandler.class);

    @Autowired
    private ServerSessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;

    @Autowired
    AppService appService;

    @Override
    public void handleMessage(VoiceFilePlayAuditCompletedEvent event) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("放音文件同步开启");
        }
        if(StringUtil.isEmpty(event.getKey())){
            List<String> apps = voiceFilePlayService.findNotSyncApp();
            for(int j=0;j<apps.size();j++){
                List<VoiceFilePlay> list = voiceFilePlayService.findNotSyncByApp(apps.get(j));
                if(list.size()>0) {//当没有需要同步的文件时，不发送请求
                    List<Map<String, Object>> list1 = null;
                    for(int i=0;i<list.size();i++) {
                        if(list1==null) {
                            list1 = new ArrayList<>();
                        }
                        if (list.get(i).getApp() != null) {
                            Map<String, Object> map = getStringObjectMap(list.get(i));
                            list1.add(map);
                        }
                        if(i!=0 && (i%10==0)) {
                            if(list1!=null&&list1.size()>0) {
                                initRequest(list1, apps.get(j));
                                list1 = null;
                            }
                        }
                    }
                    if(list1!=null&&list1.size()>0) {
                        initRequest(list1, apps.get(j));
                    }
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("应用id：["+apps.get(j)+"]没有需要同步的文件");
                    }
                }
            }
        }else{
            VoiceFilePlay voiceFilePlay = voiceFilePlayService.findById(event.getKey());
            if(voiceFilePlay!=null) {
                List<Map<String, Object>> list1 = new ArrayList<>();
                Map<String, Object> map = getStringObjectMap(voiceFilePlay);
                list1.add(map);
                initRequest(list1, voiceFilePlay.getApp().getId());
            }else{
                if(logger.isDebugEnabled()){
                    logger.debug("放音文件同步结束：当前放音文件不存在");
                }
            }
        }
    }

    private void initRequest(List<Map<String, Object>> list1,String appId) {
        String param = JSON.toJSON(list1).toString();
        if(logger.isDebugEnabled()){
            logger.debug("本次同步文件信息:{}",param);
        }
        Map<String, Object> params = new HashMap<>();
        App app = appService.findById(appId);
        String areaId = app.getAreaId();
        if(StringUtils.isBlank(areaId)){
            areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
        }
        params.put("areaId",areaId);
        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_VF_SYNC,params);
        request.setBody(param);
        try {
            rpcCaller.invoke(sessionContext,request);
            logger.info("发送放音文件指令成功");
        } catch (Exception ex) {
            logger.error("发送放音文件指令失败:"+request,ex);
        }
    }

    private Map<String, Object> getStringObjectMap(VoiceFilePlay obj) {
        Map<String, Object> map = new HashMap();
        map.put("id", obj.getId());
        map.put("appId", obj.getApp().getId());
        map.put("tenantId", obj.getTenant().getId());
        map.put("name", obj.getName());
        map.put("fileKey", obj.getFileKey());
        return map;
    }
}
