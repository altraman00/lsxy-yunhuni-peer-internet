package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.yunhuni.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by liups on 2016/8/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MainClass.class)
public class CalCostTest {

    static {
//将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    private CalCostService calCostService;
    @Autowired
    private CalBillingService calBillingService;

    @Test
    public void testCalCost(){
//        BigDecimal cost = calCostService.calCost("duo_call", "8a2bc5f656c1194c0156c46a187f0002", 60251L);
//        System.out.println(cost);
    }

    @Test
    public void testCalBilling(){
        Long balance = calBillingService.getConference("1");
        System.out.println(balance);
    }
}