package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.oss.OSSService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 放音文件/录音文件删除未成功补救方案
 * Created by zhangxb on 2016/11/30.
 */
@Component
public class DeleteOssFileTask {
    private static final Logger logger = LoggerFactory.getLogger(DeleteOssFileTask.class);
    private static String repository= SystemConfig.getProperty("global.oss.aliyun.bucket");
    @Autowired
    private OSSService ossService;
    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private VoiceFilePlayService voiceFilePlaydService;
    @Autowired
    private LineGatewayService lineGatewayService;
    @Autowired
    private RedisCacheService redisCacheService;
    /**
     * 通知进行放音文件同步操作
     */
    @Scheduled(cron="2 0/30 * * * ?")
    public void scheduled_startSync_yyyyMMddHHmm() {
        deleteOssFile();
        if(logger.isDebugEnabled()){
            logger.debug("启动放音文件/录音文件删除未成功补救方案--结束");
        }
    }
    private void deleteOssFile(){
        deleteOssPlayFile();
        deleteOssRecordFile();
    }
    private void deleteOssPlayFile(){
        try {
            List<Map> list = voiceFilePlaydService.getOssListByDeleted();
            List<String> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                int status = VoiceFilePlay.DELETED_FAIL;
                try {
                    ossService.deleteObject(repository, map.get("ossUrl"));
                    status = VoiceFilePlay.DELETED_SUCCESS;
                } catch (Exception e) {
                    logger.error("删除OSS文件：{1}失败，异常{2}", map.get("ossUrl"), e);
                }
                String sql = " update db_lsxy_bi_yunhuni.tb_bi_voice_file_play set oss_deleted='" + status + "' where id='" + map.get("id") + "' ";
                list1.add(sql);
            }
            lineGatewayService.batchModify(list1.toArray(new String[list1.size()]));
        }catch (Exception e){
            logger.debug("启动放音文件/录音文件删除未成功补救方案--删除ossPlayFile时发生异常",e);
        }
    }
    private void deleteOssRecordFile(){
        try {
            List<Map> list = voiceFileRecordService.getOssListByDeleted();
            List<String> list1 = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                Map<String, String> map = list.get(i);
                int status = VoiceFilePlay.DELETED_FAIL;
                try {
                    ossService.deleteObject(repository, map.get("ossUrl"));
                    status = VoiceFilePlay.DELETED_SUCCESS;
                } catch (Exception e) {
                    logger.error("删除OSS文件：{1}失败，异常{2}", map.get("ossUrl"), e);
                }
                String sql = " update db_lsxy_bi_yunhuni.tb_bi_voice_file_record set oss_deleted='" + status + "' where id='" + map.get("id") + "' ";
                list1.add(sql);
            }
            lineGatewayService.batchModify(list1.toArray(new String[list1.size()]));
        }catch (Exception e){
            logger.debug("启动放音文件/录音文件删除未成功补救方案--删除ossRecordFile时发生异常",e);
        }
    }
}
