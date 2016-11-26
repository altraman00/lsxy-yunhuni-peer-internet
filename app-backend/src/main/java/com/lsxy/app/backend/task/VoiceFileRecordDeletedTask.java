package com.lsxy.app.backend.task;

import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.VoiceFileRecordDeleteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 录音文件定时删除
 * Created by zhangxb on 2016/8/30.
 */
@Component
public class VoiceFileRecordDeletedTask {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFileRecordDeletedTask.class);
    @Autowired
    private MQService mqService;
    /**
     * 录音文件定时删除
     */
    @Scheduled(cron="0 0/30 * * * ?")
    public void startSync() {
        if(logger.isDebugEnabled()){
            logger.debug("启动录音文件定时删除方案--开始");
        }
        mqService.publish(new VoiceFileRecordDeleteEvent());
        if(logger.isDebugEnabled()){
            logger.debug("启动录音文件定时删除方案--已通知--结束");
        }
    }

}
