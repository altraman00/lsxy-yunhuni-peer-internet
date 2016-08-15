package com.lsxy.app.api.gateway.rest;

import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.MQStasticCounter;
import com.lsxy.framework.mq.events.apigw.APIGatewayRequestEvent;
import com.lsxy.framework.mq.events.apigw.test.TestEchoRequestEvent;
import com.lsxy.framework.mq.events.apigw.test.TestResetStasticsCountEvent;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by tandy on 16/8/10.
 * 为测试使用的控制器
 */
@Profile(value={"test","development","local"})
@RestController
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
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

    @RequestMapping("/test/mq/presure")
    public RestResponse<String> mqPressure(HttpServletRequest request){
        RestResponse<String> response = RestResponse.success();
        response.setSuccess(true);
        String requestId = UUIDGenerator.uuid();
        TestEchoRequestEvent event = new TestEchoRequestEvent(requestId,"HELLO");
        mqService.publish(event);

        response.setData("OK");
        return response;
    }

    @PostConstruct
    public void doPressureTest(){
        for(int i=0;i<1;i++){
            Thread thread = new Thread(new RunTask());
            thread.start();
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
            while(true){
                String requestId = UUIDGenerator.uuid();
                TestEchoRequestEvent event = new TestEchoRequestEvent(requestId,"HELLO");
                mqService.publish(event);

                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
