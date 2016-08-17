package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.framework.api.test.TestService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.MQStasticCounter;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.test.TestResetStasticsCountEvent;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/10.
 * 为测试使用的控制器
 */
@Profile(value={"test","development","local"})
@RestController
public class TestController {


    private static final Logger logger = LoggerFactory.getLogger(TestController.class);


    @Reference(timeout = 3000)
    private TestService testService;

    @Autowired(required = false)
    private StasticsCounter sc;
    @Autowired(required = false)
    private MQStasticCounter mqsc;

    @Autowired
    private MQService mqService;

    @Autowired
    private AsyncRequestContext asyncRequestContext;


    @RequestMapping("/test/clean/sa")
    public RestResponse cleanStasticCount(){
        if(sc != null) sc.reset();
        if(mqsc!=null) mqsc.reset();

        TestResetStasticsCountEvent trsae = new TestResetStasticsCountEvent();
        mqService.publish(trsae);
        return RestResponse.success();
    }

    @RequestMapping("/test/dubbo")
    public RestResponse<String> mqPressure(HttpServletRequest request){
        RestResponse<String> response = RestResponse.success();
        response.setSuccess(true);
//        String requestId = UUIDGenerator.uuid();
//        TestEchoRequestEvent event = new TestEchoRequestEvent(requestId,"HELLO");
//        mqService.publish(event);
        long startdt = System.currentTimeMillis();
        String xx = testService.sayHi(UUIDGenerator.uuid());
        if(logger.isDebugEnabled()){
            logger.debug("收到返回值:{},共花费:{}ms",xx,System.currentTimeMillis() - startdt);
        }

        response.setData("OK");
        return response;
    }

    @RequestMapping("/test/presure/{threads}")
    public void doPressureTest(@PathVariable int threads){
        ExecutorService es = Executors.newFixedThreadPool(threads);

        long starttime = System.currentTimeMillis();
        for(int i=0;i<threads;i++){
//            Thread thread = new Thread(new RunTask());
//            thread.start();
            es.submit(new RunTask());
        }
        es.shutdown();;
        try {
            while (!es.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                System.out.println("还在跑,线程池没有关闭");
            }

            if(logger.isDebugEnabled()){
                logger.debug("整个测试全部完成,共耗时:{}ms",System.currentTimeMillis() - starttime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class RunTask implements  Runnable{
        @Override
        public void run() {
//            while(!mqService.ready()){
//                try {
//                    TimeUnit.SECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                logger.info("mq not ready yet !  waiting ....");
//            }
            int c = 10000;
            long starttime = System.currentTimeMillis();
            int sc = 0;
            while(c -- > 0){
//                String requestId = UUIDGenerator.uuid();
//                TestEchoRequestEvent event = new TestEchoRequestEvent(requestId,"HELLO");
//                mqService.publish(event);
                try {
                    long startdt = System.currentTimeMillis();
                    String xx = testService.sayHi(UUIDGenerator.uuid());
                    if (logger.isDebugEnabled()) {
                        logger.debug("[{}]收到返回值:{},共花费:{}ms", c , xx, System.currentTimeMillis() - startdt);
                    }
                }catch (Exception ex){

                }
//                try {
//                    TimeUnit.MILLISECONDS.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
            if(logger.isDebugEnabled()){
                logger.debug("当前线程{}测试完毕,总共用时:{}ms",Thread.currentThread().getName(),System.currentTimeMillis() - starttime);
            }
        }
    }

//    public static void main(String[] args) {
//        Date stdt = DateUtils.parseDate("2016-08-17 17:49:14.641","yyyy-MM-dd HH:mm:ss.SSS");
//        Date enddt = DateUtils.parseDate("2016-08-17 18:04:26.602","yyyy-MM-dd HH:mm:ss.SSS");
//        System.out.println(enddt.getTime() - stdt.getTime() );
//    }

}
