package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Created by liups on 2016/11/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class SpringCacheTest {
    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private ACs aCs;

    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

    @Test
    public void testSpringCacheSave(){
//        callCenterAgentService.testSave();
    }

    @Test
    public void testSpringCacheDelete(){
        try {
            callCenterAgentService.delete("testCache");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
//        callCenterAgentService.testDelete();
    }


}
