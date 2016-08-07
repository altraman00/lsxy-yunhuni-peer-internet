package com.lsxy.app.api.gateway.rest;

import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tandy on 2016/6/28.
 * 呼叫API入口
 */
@RestController
public class CallController extends AbstractAPIController{
private static final Logger logger = LoggerFactory.getLogger(CallController.class);
    /**
     * 自动化测试用例使用
     * @param accountId
     * @return
     */
    @RequestMapping("/{accountId}/calltest")
    public RestResponse test(@PathVariable String accountId){
        return RestResponse.success(accountId);
    }

    @RequestMapping("/{accountId}/call")
    public RestResponse doCall(@PathVariable String accountId){
        return RestResponse.success("中文");
    }

    @RequestMapping("/{accountId}/call/{callId}")
    public RestResponse getCall(@PathVariable String accountId,@PathVariable String callId){
        return RestResponse.success(callId);
    }


        @RequestMapping("/async/test")
        @ResponseBody
        public Callable<RestResponse> callable(HttpServletResponse response) {
            if(logger.isDebugEnabled()){
                logger.debug("111111111");
            }
            // 这么做的好处避免web server的连接池被长期占用而引起性能问题，
            // 调用后生成一个非web的服务线程来处理，增加web服务器的吞吐量。
            return new Callable<RestResponse>() {
                @Override
                public RestResponse call() throws Exception {
                    if(logger.isDebugEnabled()){
                        logger.debug("2222222222");
                    }
                    TimeUnit.SECONDS.sleep(10);
                    return RestResponse.success("中文2");
                }
            };
        }

}
