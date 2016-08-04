package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/7/30.
 */

@Component
public class MyScheduledTaskForTestX {

    public MyScheduledTaskForTestX(){
    }

    @Autowired
    private Client client;

    private static final Logger logger = LoggerFactory.getLogger(MyScheduledTaskForTestX.class);


//    @Scheduled(fixedDelay=1)
//    public void doTest(){
//        if(logger.isDebugEnabled()){
//        }
//        Session session = client.getAvalibleSession();
//        if(session != null){
//            RPCRequest request = RPCRequest.newRequest("REQUEST","VALUE0=001");
//            session.write(request);
//        }
//    }

    @PostConstruct
    public void doTest() throws InterruptedException {
        if(logger.isDebugEnabled()){
            logger.debug("开始测试");
        }
        Session session = null;
        while(true){
            session = client.getAvalibleSession();
            if(session == null || !session.isValid()){
                if(logger.isDebugEnabled()){
                    logger.debug("没有有效的session,等着吧");
                }
                TimeUnit.SECONDS.sleep(1);
            }else{
                break;
            }
        }

        RPCRequest request = RPCRequest.newRequest("HELLO","param01=001");
        while(true){
            session.write(request);
        }
    }
}
