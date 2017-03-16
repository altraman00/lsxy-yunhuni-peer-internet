package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 放音文件上传遗漏补救方案
 * Created by zhangxb on 2016/8/30.
 */
@Component
public class VoiceFilePlaySyncTask {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlaySyncTask.class);
    @Autowired
    private MQService mqService;
    @Autowired
    private RedisCacheService redisCacheService;
    /**
     * 通知进行放音文件同步操作
     */
    @Scheduled(cron="0 0/30 * * * ?")
    public void scheduled_startSync_yyyyMMddHHmm() {
        //执行语句
        if(logger.isDebugEnabled()){
            logger.debug("启动放音文件上传遗漏补救方案--开始");
        }
        mqService.publish(new VoiceFilePlayAuditCompletedEvent());
        if(logger.isDebugEnabled()){
            logger.debug("启动放音文件上传遗漏补救方案--已通知--结束");
        }
    }

}
