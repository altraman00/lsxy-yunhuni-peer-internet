package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.CallService;
import com.lsxy.framework.api.test.TestService;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/16.
 */
@Service
@Component
public class TestServiceImpl implements TestService {
    public static final Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);


    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private CallService callService;

    @Override
    public String sayHi(String name) {
        if(logger.isDebugEnabled()){
            logger.debug("收到请求:{}",name);
        }
        return "hi :" + name;
    }


    @Override
    public void rpcPresureTest(int threads,int count){
        ExecutorService es = Executors.newFixedThreadPool(threads);

        long starttime = System.currentTimeMillis();
        long result = 0L;
        for(int i=0;i<threads;i++){
            es.submit(new CallTask(count));
        }
        es.shutdown();;
        try {
            while (!es.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                System.out.println("还在跑,线程池没有关闭");
            }

            result = System.currentTimeMillis() - starttime;
            if(logger.isDebugEnabled()){
                logger.debug("整个测试全部完成,共耗时:{}ms",result);
            }
        } catch (InterruptedException e) {
            logger.error("线程池异常",e);
        }
    }


    @Override
    public void testPresure(int threads,int count){
        for(int i=0;i<threads;i++){
            Thread thread = new Thread(new CallTask(count));
            thread.setName("test-"+i);
            thread.start();
        }
    }


    class CallTask implements  Runnable{
        private int count;
        public CallTask(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            for(int i=0;i<count;i++){
                RPCRequest request = RPCRequest.unserialize("RQ:4bbe6d916ca4ea057808dc970b15d2a6 1481858234226 MN_CH_SYS_CALL max_answer_seconds=21600&from_uri=system@area001.area.oneyun.com&to_uri=1000492@123.57.157.32&areaId=area001&max_ring_seconds=45&user_data=eba182f997aa287dc45ea13b709ba48b&");
                try {
                    rpcCaller.invoke(sessionContext, request);
                    Thread.currentThread().sleep(10);
                } catch (Exception ex) {
                    logger.error("调用异常：" + ex);
                }
            }
        }
    }

}
