package com.lsxy.app.backend.test;

import com.lsxy.app.backend.MainClass;
import com.lsxy.app.backend.task.*;
import com.lsxy.framework.config.Constants;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

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

    @Autowired
    ApiCallStatisticsTask apiCallStatisticsTask;
    @Autowired
    AppStatisticsTask appStatisticsTask;
    @Autowired
    ConsumeStatisticsTask consumeStatisticsTask;
    @Autowired
    RechargeStatisticsTask rechargeStatisticsTask;
    @Autowired
    VoiceCdrStatisticsTask voiceCdrStatisticsTask;

    @Test
    public void testApiStatitics(){
        apiCallStatisticsTask.hourStatistics(new Date());
        apiCallStatisticsTask.dayStatistics(new Date());
        apiCallStatisticsTask.monthStatistics(new Date());

        appStatisticsTask.hourStatistics(new Date());
        appStatisticsTask.dayStatistics(new Date());
        appStatisticsTask.monthStatistics(new Date());

        consumeStatisticsTask.hourStatistics(new Date());
        consumeStatisticsTask.dayStatistics(new Date());
        consumeStatisticsTask.monthStatistics(new Date());

        rechargeStatisticsTask.hourStatistics(new Date());
        rechargeStatisticsTask.dayStatistics(new Date());
        rechargeStatisticsTask.monthStatistics(new Date());

        voiceCdrStatisticsTask.hourStatistics(new Date());
        voiceCdrStatisticsTask.dayStatistics(new Date());
        voiceCdrStatisticsTask.monthStatistics(new Date());

    }

}
