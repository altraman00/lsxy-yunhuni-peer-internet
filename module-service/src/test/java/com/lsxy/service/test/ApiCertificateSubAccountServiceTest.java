package com.lsxy.service.test;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
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
public class ApiCertificateSubAccountServiceTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Autowired
    private ApiCertificateService apiCertificateService;

    @Test
    public void testSave(){
        ApiCertificateSubAccount account = new ApiCertificateSubAccount();
        account.setCertId("123456789");
        account.setSecretKey("123456789");
        account.setTenantId("123sdasdf");
        account.setCallbackUrl("www.baidu.com");
        apiCertificateSubAccountService.save(account);
    }

    @Test
    public void testFind(){
        ApiCertificate saccount = apiCertificateService.findById("40288aca5a3bce03015a3bce14070000");
        ApiCertificate account = apiCertificateService.findById("40288ac957e0afc80157e0b24aee0001");
        System.out.println(saccount);
        System.out.println(account);

    }


    @Test
    public void testDelete() throws InvocationTargetException, IllegalAccessException {
        apiCertificateSubAccountService.delete("40288aca5a3b5560015a3b5571de0000");
    }

    @Autowired
    ResourcesRentService resourcesRentService;

    @Test
    public void testPage(){
        long start = System.currentTimeMillis();
        Page<ResourcesRent> page = resourcesRentService.findByAppId("40288aca574060400157406427f20005", 1, 20);
        System.out.println(System.currentTimeMillis() - start);
        List<ResourcesRent> result = page.getResult();
        for(ResourcesRent rent:result){
            System.out.println(rent.getResourceTelenum().getTelNumber());
        }
    }

    @Autowired
    CertAccountQuotaService certAccountQuotaService;

    @Test
    public void testQuotaStatistics(){
        Date date = new Date();
        Date preDate = DateUtils.getPreDate(date);
        certAccountQuotaService.dayStatics(preDate);
    }

    @Autowired
    SubaccountDayService subaccountDayService;
    @Autowired
    SubaccountMonthService subaccountMonthService;
    @Test
    public void testDayStatistics(){
        Date prevMonth = DateUtils.getPrevMonth(new Date());
        subaccountMonthService.monthStatistics(new Date());
    }

}
