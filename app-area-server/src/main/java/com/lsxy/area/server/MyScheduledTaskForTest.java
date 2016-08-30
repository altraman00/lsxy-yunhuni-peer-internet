package com.lsxy.area.server;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.api.server.Session;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by tandy on 16/7/30.
 */

//@Component("scheduledTask001")
//public class MyScheduledTaskForTest {
//
//    @Autowired
//    private RemoteServer server;
//
//    private static final Logger logger = LoggerFactory.getLogger(MyScheduledTaskForTest.class);
//
//    private long last = 0;
////    @Scheduled(fixedDelay=1000)
//    public void doGetProcessCount(){
//        long thistime = server.getHandler().getProcessRequestCount();
//        logger.info("-----累计次数:{}",thistime);
//        logger.info("-----本周期内次数:{}",thistime-last);
//        last = thistime;
//    }
//
////    @Scheduled(fixedDelay=100)
//    public void doCall(){
//        if(logger.isDebugEnabled()){
//            logger.debug("发消息开始");
//        }
//        Collection<Session> sessions = server.getHandler().getSessionContext().sessions();
//        for (Session session:sessions) {
//            RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL,"ID=1&param01=sss&param02=bbbb&pama03=cccc");
//            try {
//                session.write(request);
//            } catch (SessionWriteException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
