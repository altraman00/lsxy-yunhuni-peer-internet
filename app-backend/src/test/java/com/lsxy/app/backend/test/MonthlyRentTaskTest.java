package com.lsxy.app.backend.test;

import com.lsxy.app.backend.MainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liups on 2017/1/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(MainClass.class)
public class MonthlyRentTaskTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    CallCenterStatisticsService callCenterStatisticsService;

    @Test
    public void testResourcesRentTest(){
        //执行语句
        resourcesRentService.resourcesRentTask();
    }

    @Test
    public void recordingVoiceFileTaskTest(){
        //执行语句
        voiceFileRecordService.recordingVoiceFileTask();
    }

    @Test
    public void agentMonthTaskTest(){
        //执行语句
        callCenterStatisticsService.agentMonthTask();
    }

}
