package com.lsxy.call.center.test;

import com.lsxy.call.center.CallCenterMainClass;
import com.lsxy.call.center.api.model.EnQueue;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.call.center.api.service.EnQueueService;
import com.lsxy.call.center.api.utils.EnQueueDecoder;
import com.lsxy.framework.config.Constants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutionException;

/**
 * Created by Liuws on 2016/7/14.
 * 会议测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CallCenterMainClass.class)
public class OpensipsTest {


    static {
        //将 spring boot 的默认配置文件设置为系统配置文件
        System.setProperty("spring.config.location","classpath:"+ Constants.DEFAULT_CONFIG_FILE);
    }

}
