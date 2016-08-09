package com.lsxy.app.api.gateway.rest;

import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.APIGatewayRequestEvent;
import com.lsxy.framework.mq.events.apigw.APIGatewayResponseEvent;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tandy on 2016/6/28.
 * 呼叫API入口
 */
@RestController
public class CallController extends AbstractAPIController{
private static final Logger logger = LoggerFactory.getLogger(CallController.class);

    @Autowired
    private MQService mqService;

    @Autowired
    private AsyncRequestContext asyncRequestContext;

//    /**
//     * 自动化测试用例使用
//     * @param accountId
//     * @return
//     */
//    @RequestMapping("/{accountId}/calltest")
//    public RestResponse test(@PathVariable String accountId){
//        return RestResponse.success(accountId);
//    }

    @RequestMapping("/{accountId}/call")
    public DeferredResult<RestResponse> doCall(@PathVariable String accountId, HttpServletResponse response, HttpServletRequest request){
        DeferredResult<RestResponse> result = new DeferredResult<RestResponse>(10000L,RestResponse.failed("240","服务器君有点忙,未能及时响应,抱歉啊!!  稍后再尝试!!"));

        String requestId = UUIDGenerator.uuid();


        APIGatewayRequestEvent event = new APIGatewayRequestEvent(requestId,"sys.call",WebUtils.getRequestParams(request));
        asyncRequestContext.register(event.getRequestId(),result);

        if(logger.isDebugEnabled()){
            logger.debug("发布请求事件到MQ:{}",event );
        }
        mqService.publish(event);

        return result;
    }

    @RequestMapping("/{accountId}/call/{callId}")
    public RestResponse getCall(@PathVariable String accountId,@PathVariable String callId){
        return RestResponse.success(callId);
    }
//
//
//        @RequestMapping("/async/test")
//        @ResponseBody
//        public Callable<RestResponse> callable(HttpServletResponse response) {
//            if(logger.isDebugEnabled()){
//                logger.debug("111111111");
//            }
//            // 这么做的好处避免web server的连接池被长期占用而引起性能问题，
//            // 调用后生成一个非web的服务线程来处理，增加web服务器的吞吐量。
//            return new Callable<RestResponse>() {
//                @Override
//                public RestResponse call() throws Exception {
//
//                    return RestResponse.success("中文2");
//                }
//            };
//        }

}
