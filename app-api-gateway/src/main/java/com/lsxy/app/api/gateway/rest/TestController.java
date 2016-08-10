package com.lsxy.app.api.gateway.rest;

import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.MQStasticCounter;
import com.lsxy.framework.mq.events.apigw.test.TestEchoRequestEvent;
import com.lsxy.framework.mq.events.apigw.test.TestResetStasticsCountEvent;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * Created by tandy on 16/8/10.
 * 为测试使用的控制器
 */
@Profile(value={"development","local"})
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
    public DeferredResult<RestResponse> mqPressure(){
        DeferredResult<RestResponse> result = new DeferredResult<RestResponse>(10000L,RestResponse.failed("240","服务器君有点忙,未能及时响应,抱歉啊!!  稍后再尝试!!"));

        String requestId = UUIDGenerator.uuid();

        TestEchoRequestEvent event = new TestEchoRequestEvent(requestId,"tt");
        asyncRequestContext.register(event.getRequestId(),result);

        if(logger.isDebugEnabled()){
            logger.debug("发布异步请求事件到MQ:{}",event );
        }
        mqService.publish(event);

        return result;

    }
}
