package com.lsxy.area.agent;

import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
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

//@Component
public class MyScheduledTaskForTestX implements Runnable{

    public MyScheduledTaskForTestX(){
    }


    @Autowired
    private ClientSessionContext sessionContext;

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
        Thread t = new Thread(this);
        logger.info("启动测试进程");
        t.start();
    }

    @Override
    public void run() {
            logger.info("开始测试");
        Session session = null;
        while(true){
            session = sessionContext.getAvalibleSession();
            if(session == null || !session.isValid()){
                    logger.info("没有有效的session,等着吧");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
