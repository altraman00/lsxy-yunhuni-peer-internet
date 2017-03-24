package com.lsxy.service.test;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.config.Constants;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by liups on 2017/3/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(value={FrameworkServiceConfig.class, YunhuniServiceConfig.class, FrameworkApiConfig.class, YunhuniApiConfig.class, FrameworkCacheConfig.class})
@EnableJpaRepositories(value = {"com.lsxy.framework","com.lsxy.yunhuni"})
@EnableAutoConfiguration
@SpringApplicationConfiguration(AccountServiceTest.class)
public class QuotaServiceTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    CertAccountQuotaService certAccountQuotaService;

    @Test
    public void testQuota(){
        certAccountQuotaService.incQuotaUsed("aaaa",new Date(),3L,"msg_sms");
    }
}
