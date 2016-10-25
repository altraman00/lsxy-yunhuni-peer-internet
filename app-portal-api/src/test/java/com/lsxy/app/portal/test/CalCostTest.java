package com.lsxy.app.portal.test;

import com.lsxy.app.portal.MainClass;
import com.lsxy.framework.api.tenant.model.TenantVO;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;
import com.lsxy.yunhuni.api.statistics.service.DayStaticsService;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrHourService;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.user.model.OcUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

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
    @Autowired
    VoiceCdrHourService voiceCdrHourService;
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    DayStaticsService dayStaticsService;
    @Autowired
    TenantService tenantService;

    @Test
    public void testCalCost(){
//        BigDecimal cost = calCostService.calCost("duo_call", "8a2bc5f656c1194c0156c46a187f0002", 60251L);
//        System.out.println(cost);
    }

    @Test
    public void testCalBilling(){
        Date date = DateUtils.parseDate("2016-10-07", "yyyy-MM-dd");
        Date date1 = DateUtils.parseDate("2016-10-08", "yyyy-MM-dd");
        BigDecimal balance = calBillingService.getBalance("40288aca574060400157406339080002",date,date1,new BigDecimal(100.0));
        System.out.println(balance);
    }

    @Test
    public void calAverageCall(){
        Map<String, Object> map = voiceCdrHourService.calAverageCall("1");
        System.out.println(map.get("lineAverageCallTime"));
        System.out.println(map.get("lineLinkRate"));
    }
    @Test
    public void testResourcesRent(){

        resourcesRentService.resourcesRentTask();
    }

    @Test
    public void testDayStatics(){
        String type = "month";
        DayStatics statics;
        if("today".equals(type)){
            statics = calBillingService.getIncStaticsOfCurrentDay("40288ac9575612a30157561c7ff50004");
        }else  if("month".equals(type)){
            statics = calBillingService.getIncStaticsOfCurrentMonth("40288ac9575612a30157561c7ff50004");
        }else{
            statics = calBillingService.getCurrentStatics("40288ac9575612a30157561c7ff50004");
        }
        System.out.println(JSONUtil2.objectToJson(statics));
//        dayStaticsService.dayStatics(DateUtils.getPreDate(new Date()));
    }

    @Test
    public void testTenantList(){
        Page<TenantVO> tenantVOPage = tenantService.pageListBySearch(null, null, null, null, null, 1, 20);
        System.out.println(tenantVOPage);
    }
}
