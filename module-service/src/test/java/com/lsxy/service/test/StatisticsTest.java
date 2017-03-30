package com.lsxy.service.test;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.statistics.service.MsgDayService;
import com.lsxy.yunhuni.api.statistics.service.MsgMonthService;
import com.lsxy.yunhuni.api.statistics.service.SubaccountDayService;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/2/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(value={FrameworkServiceConfig.class, YunhuniServiceConfig.class, FrameworkApiConfig.class, YunhuniApiConfig.class, FrameworkCacheConfig.class})
@EnableJpaRepositories(value = {"com.lsxy.framework","com.lsxy.yunhuni"})
@EnableAutoConfiguration
@SpringApplicationConfiguration(AccountServiceTest.class)
public class StatisticsTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    MsgDayService msgDayService;
    @Autowired
    MsgMonthService ssgMonthService;

    @Test
    public void msgTest(){
        Date date=new Date();
        //执行语句
        Date preDate = DateUtils.getPreDate(date);
        msgDayService.dayStatistics(preDate);
//        ssgMonthService.monthStatistics(new Date());
    }

}
