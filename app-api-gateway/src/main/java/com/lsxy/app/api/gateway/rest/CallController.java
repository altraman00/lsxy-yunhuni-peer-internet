package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.area.api.DuoCallbackVO;
import com.lsxy.area.api.CallService;
import com.lsxy.area.api.exceptions.InvokeCallException;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tandy on 2016/6/28.
 * 呼叫API入口
 */
@RestController
public class CallController extends AbstractAPIController{
private static final Logger logger = LoggerFactory.getLogger(CallController.class);

    @Autowired
    private MQService mqService;

    @Autowired(required = false)
    private StasticsCounter sc;

    @Reference(timeout=3000)
    private CallService callService;

    @Autowired
    private AsyncRequestContext asyncRequestContext;

    @Autowired
    private AppService appService;

//    /**
//     * 自动化测试用例使用
//     * @param accountId
//     * @return
//     */
//    @RequestMapping("/{accountId}/calltest")
//    public RestResponse test(@PathVariable String accountId){
//        return RestResponse.success(accountId);
//    }
//@RequestMapping("/{accountId}/call")
//public DeferredResult<RestResponse> doCall(@PathVariable String accountId, HttpServletResponse response, HttpServletRequest request){

    /**
     *  语音呼叫API
     *  api.dev.yunhuni.com/v1/account/1234567/call?to=13971068693&maxAnswerSec=10&maxRingSec=20
     * @param certId   鉴权账号
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/{certId}/call")
    public RestResponse<String> doCall(@PathVariable String certId, HttpServletResponse response, HttpServletRequest request,String to,String from,int maxAnswerSec,int maxRingSec){
        if(logger.isDebugEnabled()){
            WebUtils.logRequestParams(request);
        }

        /*发送请求次数计数*/
        if(sc!=null)sc.getSendGWRequestCount().incrementAndGet();

        try {
            String callid = callService.call(from,to,maxAnswerSec,maxRingSec);
            return RestResponse.success(callid);
        } catch (InvokeCallException e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
    }

    @RequestMapping("/{accountId}/call/{callId}")
    public RestResponse getCall(@PathVariable String accountId,@PathVariable String callId){
        return RestResponse.success(callId);
    }

    @RequestMapping("/{account_id}/call/duo_callback")
    public RestResponse duoCallback(HttpServletRequest request,@RequestBody DuoCallbackVO duoCallbackVO,@PathVariable String account_id) {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String callId = null;
        try {
            callId = callService.duoCallback(ip,appId, duoCallbackVO);
        } catch (InvokeCallException e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        result.put("user_data",duoCallbackVO.getUser_data());
        return RestResponse.success(result);
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


//    public static void main(String[] args) {
//        RestRequest restRequest = RestRequest.buildRequest();
//        Map<String,Object> hashMap =  new HashedMap();
//        hashMap.put("res_id","5566778");
//        RestResponse<String> response = restRequest.post("http://www.yunhuni.cn:3000/incoming",hashMap);
//        System.out.println(response.getData());
//    }
}
