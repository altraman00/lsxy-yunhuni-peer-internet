package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.framework.api.test.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/8/16.
 */
@Service
@Component
public class TestServiceImpl implements TestService {
    public static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    @Override
    public String sayHi(String name) {
        if(logger.isDebugEnabled()){
            logger.debug("收到请求:{}",name);
        }
        return "hi :" + name;
    }
}
