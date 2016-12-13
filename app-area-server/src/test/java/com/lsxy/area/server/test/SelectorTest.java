package com.lsxy.area.server.test;

import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.AreaServerMainClass;
import com.lsxy.framework.config.Constants;
import com.lsxy.framework.core.exceptions.api.AppOffLineException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public void testSelector() throws AppOffLineException, UnsupportedEncodingException {
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        for(int i=0;i<100;i++){
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
        App app = appService.findById("8a2bc67258cdafa80158cdbc57180000");
        AreaAndTelNumSelector.Selector selector = null;
        try {
            selector = areaAndTelNumSelector.getTelnumberAndAreaId(app, "02066304057", "02082241195");
        } catch (YunhuniApiException e) {
            e.printStackTrace();
        }
//        System.out.println(JSONUtil.objectToJson(selector));
        Long end = System.currentTimeMillis();
        System.out.println("方法执行时间："  + (end-start) + "ms");
    }

}
