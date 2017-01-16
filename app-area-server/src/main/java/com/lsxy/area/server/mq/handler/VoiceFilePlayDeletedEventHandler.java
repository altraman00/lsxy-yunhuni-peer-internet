package com.lsxy.area.server.mq.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.mq.events.portal.VoiceFilePlayDeleteEvent;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.jms.JMSException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/9/12.
 * 用户中心删除对应的放音文件后，通知区域删除对应放音文件
 */
@Component
public class VoiceFilePlayDeletedEventHandler implements MQMessageHandler<VoiceFilePlayDeleteEvent> {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayDeletedEventHandler.class);
    //文件保存地址 area.agent.file.play "/data/prd/p/0"
    private static final String path = SystemConfig.getProperty("area.agent.file.play");
    @Autowired
    private VoiceFilePlayService voiceFilePlayService;
    @Autowired
    private ServerSessionContext sessionContext;
    @Autowired
    AppService appService;

    @Autowired
    private RPCCaller rpcCaller;
    @Override
    public void handleMessage(VoiceFilePlayDeleteEvent event) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("删除放音文件开启");
        }
        String file = "";
        Map<String,Object> map = new HashMap<>();
        map.put("type",event.getType());
        if(VoiceFilePlayDeleteEvent.FILE.equals(event.getType())){
            map.put("id",event.getKey());
            if(logger.isDebugEnabled()){
                logger.debug("本次删除单个文件:{}",event);
            }
            //删除单个文件
            String type = event.getName().substring(event.getName().lastIndexOf("."), event.getName().length()).toLowerCase();
            file = path+"/"+event.getTenantId()+"/"+event.getAppId()+"/"+event.getKey()+type;
        }else if(VoiceFilePlayDeleteEvent.APP.equals(event.getType())){
            map.put("id",event.getAppId());
            //删除文件夹
            if(logger.isDebugEnabled()){
                logger.debug("本次删除文件夹:{}",event);
            }
            file = path +"/" +event.getTenantId()+"/"+event.getAppId();
        }else{
            if(logger.isDebugEnabled()){
                logger.debug("本次删除文件传送类型有误:{}",event);
            }
        }
        map.put("filePath",file);
        if(StringUtils.isEmpty(file)){
            if(logger.isDebugEnabled()){
                logger.debug("本次删除文件地址为空，不发事件，参数{}",event);
            }
        }else{
            //通知区域代理删除文件
            String param = JSON.toJSON(map).toString();
            if(logger.isDebugEnabled()){
                logger.debug("本次删除文件/文件夹信息:{}",param);
            }
            Map<String, Object> params = new HashMap<>();
            App app = appService.findById(event.getAppId());
            params.put("areaId",app.getAreaId());
            RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_VF_DELETED,params);
            request.setBody(param);
            try {
                rpcCaller.invoke(sessionContext,request);
                logger.info("发送本次删除文件/文件夹指令成功");
            } catch (Exception ex) {
                logger.error("发送本次删除文件/文件夹指令失败:"+request,ex);
            }
        }

    }
}
