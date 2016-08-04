package com.lsxy.area.server;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.client.Client;
import com.lsxy.framework.rpc.api.server.RemoteServer;
import com.lsxy.framework.rpc.api.server.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by tandy on 16/7/30.
 */

@Component("scheduledTask001")
public class MyScheduledTaskForTest {

    @Autowired
    private RemoteServer server;

    private static final Logger logger = LoggerFactory.getLogger(MyScheduledTaskForTest.class);
    @Scheduled(fixedDelay=5000)
    public void doTest(){

        if(logger.isDebugEnabled()){
            logger.debug("测试测试");
        }

        Collection<Session> sessions = server.getHandler().getSessionContext().sessions();
        if(sessions != null && sessions.size()>0){
            for (Session session:sessions) {
                RPCRequest request = new RPCRequest();
                request.setName("HAHAHAHA");
                request.setParam("id=100");
                request.setSessionid(UUIDGenerator.uuid());
                session.write(request);
            }
        }
    }

    private long last = 0;
    @Scheduled(fixedDelay=1000)
    public void doGetProcessCount(){
        long thistime = server.getHandler().getProcessRequestCount();
        logger.info("-----累计次数:{}",thistime);
        logger.info("-----本周期内次数:{}",thistime-last);
        last = thistime;
    }
}
