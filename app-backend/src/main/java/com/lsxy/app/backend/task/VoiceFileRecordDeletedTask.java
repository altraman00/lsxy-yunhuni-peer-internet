package com.lsxy.app.backend.task;

import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 录音文件定时删除
 * Created by zhangxb on 2016/8/30.
 */
@Component
public class VoiceFileRecordDeletedTask {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFileRecordDeletedTask.class);
//    @Autowired
//    private MQService mqService;
    private static Pattern pattern = Pattern.compile("^[0-9]*[1-9][0-9]*$");
    private static String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    private TenantConfigService tenantConfigService;
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private AppService appService;
    @Autowired
    private OSSService ossService;
    /**
     * 录音文件定时删除
     */
    @Scheduled(cron="0 0/30 * * * ?")
    public void startSync() {
        if(logger.isDebugEnabled()){
            logger.debug("启动录音文件定时删除方案--开始");
        }
//        mqService.publish(new VoiceFileRecordDeleteEvent());
        delete();
        if(logger.isDebugEnabled()){
            logger.debug("启动录音文件定时删除方案--结束");
        }
    }
    private void delete(){
        //获取全部租户
        List<Tenant> tenants = tenantService.getListByPage();
        if(tenants==null||tenants.size()==0){
            if(logger.isDebugEnabled()){
                logger.debug("删除录音文件结束，当前没有租户存在");
            }
            return ;
        }
        //获取全局时间
        GlobalConfig globalConfig = globalConfigService.findByTypeAndName(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING);
        int globalTime = 7;//如果没有找到有效全局配置，将使用7为默认免费时长
        if(globalConfig!=null) {
            Matcher matcher = pattern.matcher(globalConfig.getValue());
            if (!matcher.matches()) {
                //配置不是正整数则结束
                if (logger.isDebugEnabled()) {
                    logger.debug("没有找到有效的全局配置type[" + GlobalConfig.TYPE_RECORDING + "]keyName[" + GlobalConfig.KEY_RECORDING + "]value[" + globalConfig.getValue() + "]");
                }
            }else {
                globalTime = Integer.valueOf(globalConfig.getValue());
            }
        }else{
            if (logger.isDebugEnabled()) {
                logger.debug("查找不到全局配置type[" + GlobalConfig.TYPE_RECORDING + "]keyName[" + GlobalConfig.KEY_RECORDING + "]");
            }
        }
        //遍历全部用户
        for(int i=0;i<tenants.size();i++) {
            Tenant tenant1 = tenants.get(i);
            //获取用户名下的应用
            List<App> apps = appService.getAppsByTenantId(tenant1.getId());
            for(int ai=0;ai<apps.size();ai++){
                //获取要删除文件的创建时间
                Date createTime = getCreateDate(globalTime,tenant1.getId(),apps.get(ai).getId());
                if(createTime==null){
                    continue;
                }
                //获取租户对应的录音文件
                List<VoiceFileRecord> list2 = voiceFileRecordService.getListByTenantAndAppAndCreateTime(tenant1.getId(),apps.get(ai).getId(),createTime);
                for(int j=0;j<list2.size();j++){
                    VoiceFileRecord temp = list2.get(i);
                    //已同步oss 并且存在oss文件，并且未删除
                    if((temp.getStatus()!=null&&temp.getStatus()==1)&&StringUtils.isNotEmpty(temp.getOssUrl())&&(temp.getOssDeleted()==null||temp.getOssDeleted()!=1)){//需要删除oss文件
                        try {
                            ossService.deleteObject(repository, temp.getOssUrl());
                            temp.setOssDeleted(VoiceFilePlay.DELETED_SUCCESS);
                        }catch(Exception e){
                            logger.error("删除OSS文件：{1}失败，异常{2}",temp.getOssUrl(),e);
                            temp.setOssDeleted(VoiceFilePlay.DELETED_FAIL);
                        }
                    }
                    temp = voiceFileRecordService.save(temp);
                    try {
                        voiceFileRecordService.delete(temp);
                    } catch (Exception e) {
                        logger.error("删除录音文件记录：{1}失败，异常{2}",temp.getId(),e);
                    }
                }
            }
        }
    }
    /**删除时间*/
    private Date getCreateDate(int globalTime, String tenantId, String appId) {
        //获取租户过期的时间
        TenantConfig tenantConfig = tenantConfigService.findByTypeAndKeyNameAndTenantIdAndAppId(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING,tenantId,appId);
        if(tenantConfig!=null) {
            int tenantTime = 0;
            Matcher matcher1 = pattern.matcher(tenantConfig.getValue());
            if (matcher1.matches()) {
                tenantTime = Integer.valueOf(tenantConfig.getValue());
            }
            //得到租户下的租户下的录音文件有效时间
            int timeLong = (tenantTime > globalTime ? tenantTime : globalTime) - 1;
            //获取日期对象
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -timeLong);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            //获取删除时间
            return cal.getTime();
        }else{
            return null;
        }
    }
}
