package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by tandy on 16/7/30.
 */

@Component
public class MyScheduledTaskForTest {

    @Autowired
    private Client client;

    private static final Logger logger = LoggerFactory.getLogger(MyScheduledTaskForTest.class);
    @Scheduled(fixedDelay=5000)
    public void doTest(){

        if(logger.isDebugEnabled()){
            logger.debug("测试测试");
        }
        Session session = client.getAvalibleSession();
    }
}
