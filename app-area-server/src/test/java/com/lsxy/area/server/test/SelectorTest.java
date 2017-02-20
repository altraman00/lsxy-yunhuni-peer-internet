package com.lsxy.area.server.test;

import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.exceptions.api.AppOffLineException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayVO;
import com.lsxy.yunhuni.api.config.service.LineGatewayToPublicService;
import com.lsxy.yunhuni.api.config.service.LineGatewayToTenantService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by liups on 2016/10/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AreaServerMainClass.class)
public class SelectorTest {
    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Autowired
    AreaAndTelNumSelector areaAndTelNumSelector;
    @Autowired
    AppService appService;

    @Test
    public void testSelector() throws AppOffLineException, UnsupportedEncodingException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        for(int i=0;i<100;i++){
            Thread.sleep(20);
            results.add(executorService.submit(() -> this.select()));
        }
        for(Future f : results){
            try {
                f.get();
            }catch (Throwable t){

            }
        }
        executorService.shutdown();

    }

    private void select(){
        Long start = System.currentTimeMillis();
        App app = appService.findById("40288aca574060400157406427f20005");
        AreaAndTelNumSelector.Selector selector = null;
        try {
            selector = areaAndTelNumSelector.getTelnumberAndAreaId(null,app, null, "02082241195");
            System.out.println(JSONUtil.objectToJson(selector));
        } catch (YunhuniApiException e) {
            e.printStackTrace();
        }
//        System.out.println(JSONUtil.objectToJson(selector));
        Long end = System.currentTimeMillis();
        System.out.println("****************************************************方法执行时间："  + (end-start) + "ms");
    }

    @Autowired
    LineGatewayToPublicService lineGatewayToPublicService;

    @Autowired
    LineGatewayToTenantService lineGatewayToTenantService;
    @Test
    public void testFindLine(){
//        lineGatewayToPublicService.findAllLineGatewayByAreaId("area001");
        List area001 = lineGatewayToTenantService.findByTenantIdAndAreaId("40288ac9575612a30157561c7ff50004", "area001");
//        List<LineGatewayVO> lineGatewayByTenantId = lineGatewayToPublicService.findAllLineGatewayByAreaId("area001");
        System.out.println(JSONUtil.objectToJson(area001));
    }

    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Test
    public void testNum(){
        List<LineGatewayVO> gs = lineGatewayToPublicService.findAllLineGatewayByAreaId("area001");
        List<String> collect = gs.stream().map(LineGatewayVO::getId).collect(Collectors.toList());
        for(int i=0;i<10;i++){
            Long start = System.currentTimeMillis();
            ResourceTelenum num = resourceTelenumService.findOneFreeDialingNumber(collect);
            Long end = System.currentTimeMillis();
            System.out.println("****************************************************方法执行时间："  + (end-start) + "ms");
            System.out.println(num.getTelNumber());
        }
    }

}
