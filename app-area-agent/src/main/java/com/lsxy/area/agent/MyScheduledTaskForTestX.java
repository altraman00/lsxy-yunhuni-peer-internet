package com.lsxy.area.agent;

import ch.qos.logback.core.util.TimeUtil;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.client.ClientSessionContext;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.HaveNoExpectedRPCResponseException;
import com.lsxy.framework.rpc.exceptions.RequestTimeOutException;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
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
public class MyScheduledTaskForTestX implements Runnable{

    public MyScheduledTaskForTestX(){
    }


    @Autowired
    private ClientSessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

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
        logger.info("启动测试进程");
        for(int i=0;i<200;i++){
            Thread t = new Thread(this);
            t.start();
        }

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


        while(true){
            try {
                RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_TEST_ECHO,"param01=001");
                RPCResponse response = rpcCaller.invokeWithReturn(session,request);
                long currentTime = System.currentTimeMillis();
                logger.info("收到响应[{}],花费:{}ms   [{}]  vs [{}]",response,currentTime-response.getTimestamp(),response.getTimestamp(),currentTime);
//                TimeUnit.SECONDS.sleep(1);
            } catch (SessionWriteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RequestTimeOutException e) {
                e.printStackTrace();
            } catch (HaveNoExpectedRPCResponseException e) {
                e.printStackTrace();
            }
        }
    }
}
