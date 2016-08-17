package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.framework.api.test.TestService;
import org.springframework.stereotype.Component;

import static com.lsxy.framework.mq.ons.OnsMQService.logger;

/**
 * Created by tandy on 16/8/16.
 */
@Service(parameters = {"session=300000"})
@Component
public class TestServiceImpl implements TestService {
    @Override
    public String sayHi(String name) {
        if(logger.isDebugEnabled()){
            logger.debug("收到请求:{}",name);
        }
        return "hi :" + name;
    }
}
