package com.lsxy.msg.test;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.dubbo.EnableDubboConfiguration;
import com.lsxy.framework.monitor.FrameworkMonitorConfig;
import com.lsxy.msg.MsgServiceConfig;
import com.lsxy.msg.api.MsgApiConfig;
import com.lsxy.msg.api.model.MsgSendDetail;
import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgSendDetailService;
import com.lsxy.msg.api.service.MsgSendRecordService;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.api.statistics.service.MsgDayService;
import com.lsxy.yunhuni.api.statistics.service.MsgMonthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * Created by liups on 2017/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import({FrameworkApiConfig.class,FrameworkServiceConfig.class, FrameworkCacheConfig.class, YunhuniApiConfig.class, YunhuniServiceConfig.class,
        FrameworkMonitorConfig.class,MsgApiConfig.class,MsgServiceConfig.class})
@EnableJpaRepositories(value = {"com.lsxy.framework","com.lsxy.yunhuni"})
@EnableAutoConfiguration
@SpringApplicationConfiguration(MsgTest.class)
@EnableDubboConfiguration
public class MsgTest {
    public static final String systemId = "third.join.gateway";

    static {
        System.setProperty("systemId",systemId);
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Bean(name = "systemId")
    public String systemIdBean(){
        return systemId;
    }



    @Autowired
    MsgSendRecordService msgSendRecordService;

    @Test
    public void test(){
        List<MsgSendRecord> ussdSendOneFailAndSendNotOver = msgSendRecordService.findUssdSendOneFailAndSendNotOver();
    }


    @Autowired
    MsgSendDetailService msgSendDetailService;
    @Test
    public void testDe(){
//        msgSendRecordService.updateStateAndTaskIdById("ec1a1c062f021d415f3e77e9dd8552e5",MsgSendRecord.STATE_WAIT,"C757585745569844");
//        msgSendDetailService.updateStateAndTaskIdAndEndTimeByRecordIdAndPhones("ec1a1c062f021d415f3e77e9dd8552e5", Arrays.asList("18620591870","17620010850"),0,"C757585745569844",new Date());
        msgSendDetailService.updateStateAndTaskIdAndEndTimeByRecordIdAndPhones("ec1a1c062f021d415f3e77e9dd8552e5",new ArrayList<>(), MsgSendDetail.STATE_FAIL,"C757585745569844",new Date());

    }




}
