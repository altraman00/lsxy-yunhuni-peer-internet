package com.lsxy.area.server.mq.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.portal.VoiceFilePlayDeleteEvent;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.model.TenantConfig;
import com.lsxy.yunhuni.api.config.service.AreaService;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.*;

/**
 * Created by zhangxb on 2016/9/12.
 * 用户中心删除对应的放音文件后，通知区域删除对应放音文件
 */
@Component
public class VoiceFileRecordDeletedEventHandler implements MQMessageHandler<VoiceFilePlayDeleteEvent> {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFileRecordDeletedEventHandler.class);
    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private ServerSessionContext sessionContext;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private TenantConfigService tenantConfigService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private RPCCaller rpcCaller;
    @Autowired
    private OSSService ossService;
    @Autowired
    private AreaService areaService;
    @Override
    public void handleMessage(VoiceFilePlayDeleteEvent event) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("删除录音文件开启");
        }
        //获取全部租户
        List<Tenant> tenants = tenantService.getListByPage();
        if(tenants==null||tenants.size()==0){
            if(logger.isDebugEnabled()){
                logger.debug("删除录音文件结束，当前没有应用存在");
            }
            return ;
        }
        //获取全局时间
        GlobalConfig globalConfig = globalConfigService.findByTypeAndName(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING);
        int globalTime = Integer.valueOf(globalConfig.getValue());
        globalTime --;
        for(int i=0;i<tenants.size();i++) {
            //获取租户过期的时间
            TenantConfig tenantConfig = tenantConfigService.findByTypeAndKeyNameAndTenantId(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING,tenants.get(i).getId());
            int tenantTime = 0;
            if(tenantConfig!=null&&StringUtil.isNotEmpty(tenantConfig.getValue())){
                try {
                    tenantTime = Integer.valueOf(tenantConfig.getValue());
                    tenantTime--;
                }catch (Exception e){
                    logger.error("获取，[{}]租户[{}]的录音文件有效期报错",tenants.get(i).getId(),tenants.get(i).getTenantName());
                }
            }
            int timeLong = tenantTime>0?tenantTime:globalTime;
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,-timeLong);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND,0);
            //获取删除时间
            Date createTime = cal.getTime();
            //获取租户对应的录音文件
            List<VoiceFileRecord> list2 = voiceFileRecordService.getListByCreateTimeAndTenantId(createTime,tenants.get(i).getId());
            for(int j=0;j<list2.size();j++){
                VoiceFileRecord temp = list2.get(i);
                if(temp.getOssDeleted()==1&& StringUtils.isNotEmpty(temp.getOssUrl())){//需要删除oss文件
                    try {
                        String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
                        ossService.deleteObject(repository, temp.getOssUrl());
                        temp.setOssDeleted(VoiceFilePlay.DELETED_SUCCESS);
                    }catch(Exception e){
                        logger.error("删除OSS文件：{1}失败，异常{2}",temp.getOssUrl(),e);
                        temp.setOssDeleted(VoiceFilePlay.DELETED_FAIL);
                    }
                }
                //标识文件记录需要删除
                temp.setIsDeleted(VoiceFileRecord.IS_DELETED_TRUE);
                voiceFileRecordService.save(temp);
                try {
                    voiceFileRecordService.delete(temp);
                } catch (Exception e) {
                    logger.error("删除录音文件记录：{1}失败，异常{2}",temp.getId(),e);
                }
            }
            //获取租户需要删除的区域
            List<String> list3 = voiceFileRecordService.getAAAreaByCreateTimeAndTenantId(createTime, tenants.get(i).getId());
            if(list3==null|| list3.size()==0){
                logger.info("删除录音文件记录：[{}]租户[{}]没有需要删除的录音文件", tenants.get(i).getId(), tenants.get(i).getTenantName());
            }
            for(int k=0;k<list3.size();k++){
                Area area = areaService.findById(list3.get(i));
                if(area!=null) {
                    List<Map> list4 = voiceFileRecordService.getAAListByCreateTimeAndTenantIdAndAreaId(createTime, tenants.get(i).getId(), list3.get(i));
                    //通知区域代理删除文件
                    String param = JSON.toJSON(list4).toString();
                    if (logger.isDebugEnabled()) {
                        logger.debug("本次删除录音文件信息:{}", param);
                    }
                    Map<String, Object> params = new HashMap<>();
                    params.put("areaId ", area.getId());
                    RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_RF_DELETED, params);
                    request.setBody(param);
                    try {
                        rpcCaller.invoke(sessionContext, request);
                        logger.info("发送本次删除录音文件信息指令成功");
                    } catch (Exception ex) {
                        logger.error("发送本次删除录音文件信息指令失败:" + request, ex);
                    }
                }
            }
        }

    }
}
