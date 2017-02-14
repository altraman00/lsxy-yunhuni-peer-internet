package com.lsxy.service.test;

import com.lsxy.framework.FrameworkServiceConfig;
import com.lsxy.framework.api.FrameworkApiConfig;
import com.lsxy.framework.cache.FrameworkCacheConfig;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.yunhuni.YunhuniServiceConfig;
import com.lsxy.yunhuni.api.YunhuniApiConfig;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;

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
        ApiCertificateSubAccount account = apiCertificateSubAccountService.findById("40288aca5a3b5560015a3b5571de0000");
        System.out.println(JSONUtil.objectToJson(account));
    }


    @Test
    public void testDelete() throws InvocationTargetException, IllegalAccessException {
        apiCertificateSubAccountService.delete("40288aca5a3b5560015a3b5571de0000");
    }

}
