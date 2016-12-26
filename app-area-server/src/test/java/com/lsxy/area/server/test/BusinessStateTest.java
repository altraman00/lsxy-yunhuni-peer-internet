package com.lsxy.area.server.test;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.utils.MapBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liuws on 2016/10/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaServerMainClass.class)
public class BusinessStateTest {

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }
    @Autowired
    private BusinessStateService businessStateService;

    @Test
    public void test() throws InterruptedException {
        BusinessState state = new BusinessState.Builder()
                .setTenantId("11111111111")
                .setAppId("222222222")
                .setId("xxxxxxxxx1")
                .setType(BusinessState.TYPE_CC_OUT_CALL)
                .setUserdata("fwqfqwfqw")
                .setCallBackUrl("wegwegwegw")
                .setAreaId("gwegwegwegweg")
                .setLineGatewayId("gwegwegweg")
                .setBusinessData(new MapBuilder<String,String>()
                        .put("111111","111111")
                        .put("222222","222222")
                        .build())
                .build();

        businessStateService.save(state);
        System.out.println(businessStateService.get("xxxxxxxxx1"));;
        businessStateService.updateInnerField("xxxxxxxxx1","111111","3333333");
        System.out.println(businessStateService.get("xxxxxxxxx1"));;
        businessStateService.updateResId("xxxxxxxxx1","res_id");
        businessStateService.updateAreaId("xxxxxxxxx1","area_id");
        businessStateService.updateCallBackUrl("xxxxxxxxx1","callback");
        businessStateService.updateClosed("xxxxxxxxx1",true);
        businessStateService.updateLineGatewayId("xxxxxxxxx1","line");
        businessStateService.updateUserdata("xxxxxxxxx1","userdata");
        System.out.println(businessStateService.get("xxxxxxxxx1"));;
    }

}
