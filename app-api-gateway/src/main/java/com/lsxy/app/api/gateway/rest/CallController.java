package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.DuoCallbackDTO;
import com.lsxy.app.api.gateway.dto.NotifyCallDTO;
import com.lsxy.app.api.gateway.dto.VerifyCallInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.area.api.CallService;
import com.lsxy.framework.core.exceptions.api.DuoCallbackNumIsSampleException;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.utils.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tandy on 2016/6/28.
 * 呼叫API入口
 */
@RestController
public class CallController extends AbstractAPIController{
    private static final Logger logger = LoggerFactory.getLogger(CallController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private CallService callService;

//    @Autowired
//    private MQService mqService;
//
//    @Autowired(required = false)
//    private StasticsCounter sc;
//
//    @Autowired
//    private AsyncRequestContext asyncRequestContext;
//
//    @Autowired
//    private AppService appService;

//    /**
//     * 自动化测试用例使用
//     * @param accountId
//     * @return
//     */
//    @RequestMapping("/{accountId}/calltest")
//    public ApiGatewayResponse test(@PathVariable String accountId){
//        return ApiGatewayResponse.Success(accountId);
//    }
//@RequestMapping("/{accountId}/call")
//public DeferredResult<ApiGatewayResponse> doCall(@PathVariable String accountId, HttpServletResponse response, HttpServletRequest request){

//    /**
//     *  语音呼叫API
//     *  api.dev.yunhuni.com/v1/account/1234567/call?to=13971068693&maxAnswerSec=10&maxRingSec=20
//     * @param certId   鉴权账号
//     * @param response
//     * @param request
//     * @return
//     */
//    @RequestMapping("/{certId}/call")
//    public ApiGatewayResponse<String> doCall(@PathVariable String certId, HttpServletResponse response, HttpServletRequest request, String to, String from, int maxAnswerSec, int maxRingSec) throws YunhuniApiException {
//        if(logger.isDebugEnabled()){
//            WebUtils.logRequestParams(request);
//        }
//        /*发送请求次数计数*/
//        if(sc!=null)sc.getSendGWRequestCount().incrementAndGet();
//
//        String callid = callService.call(from,to,maxAnswerSec,maxRingSec);
//        return ApiGatewayResponse.success(callid);
//
//    }
//
//    @RequestMapping("/{accountId}/call/{callId}")
//    public ApiGatewayResponse getCall(@PathVariable String accountId,@PathVariable String callId){
//        return ApiGatewayResponse.success(callId);
//    }

    @RequestMapping(value = "/{account_id}/call/duo_callback",method = RequestMethod.POST)
    public ApiGatewayResponse duoCallback(HttpServletRequest request, @Valid @RequestBody DuoCallbackDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String callId;
        String to1 = dto.getTo1();
        String to2 = dto.getTo2();
        if(to1.equals(to2)){
            //语音回拔两个号码不能是同一个
            throw new DuoCallbackNumIsSampleException();
        }
        //TODO 暂不支持模式为1
        if(dto.getRing_tone_mode() != null && dto.getRing_tone_mode() == 1){
            throw new RequestIllegalArgumentException();
        }

        callId = callService.duoCallback(ip,appId,dto.getFrom1(),dto.getTo1(),dto.getFrom2(),dto.getTo2(),dto.getRing_tone(),dto.getRing_tone_mode(),dto.getMax_dial_duration(),
                dto.getMax_call_duration(),dto.getRecording(),dto.getRecord_mode(),dto.getUser_data());

        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        result.put("user_data", dto.getUser_data());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/call/duo_callback_cancel",method = RequestMethod.POST)
    public ApiGatewayResponse duoCallbackCancel(HttpServletRequest request, @RequestBody Map params, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        callService.duoCallbackCancel(ip,appId, (String)params.get("callId"));
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/call/notify_call",method = RequestMethod.POST)
    public ApiGatewayResponse notifyCall(HttpServletRequest request, @Valid @RequestBody NotifyCallDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        //参数校验
        if(StringUtils.isBlank(dto.getTo())){
            throw new RequestIllegalArgumentException();
        }

        //将二维数组是为空的数组去掉
        List<List<Object>> playContent = dto.getPlay_content();
        if(playContent != null){
            for(int i = 0;i < playContent.size();i++){
                List<Object> list = playContent.get(i);
                if(list.size()< 2 || list.get(0)==null || "".equals(list.get(0))|| list.get(1)==null || "".equals(list.get(1))){
                    playContent.remove(i);
                    i--;
                }
            }
        }
        //播放内容不能为空
        if(StringUtil.isBlank(dto.getPlay_file()) && (dto.getPlay_content() == null || dto.getPlay_content().size() == 0 )){
            throw new RequestIllegalArgumentException();
        }

        String callId = callService.notifyCall(ip,appId, dto.getFrom(),dto.getTo(),dto.getPlay_file(),
                dto.getPlay_content(),dto.getRepeat(),dto.getMax_dial_duration(),dto.getUser_data());
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        result.put("user_data", dto.getUser_data());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/call/verify_call",method = RequestMethod.POST)
    public ApiGatewayResponse verify_call(HttpServletRequest request, @PathVariable String account_id,
            @RequestHeader("AppID") String appId, @Valid @RequestBody VerifyCallInputDTO dto) throws YunhuniApiException {
        String ip = WebUtils.getRemoteAddress(request);

        if(logger.isDebugEnabled()){
            logger.debug("VERIFY CALL API参数,appId={},dto={}",appId,dto);
        }

        String callId = callService.verifyCall(ip,appId, dto.getFrom(),dto.getTo(),dto.getMaxDialDuration(),dto.getVerifyCode(),dto.getPlayFile(),dto.getRepeat(),dto.getUserData());
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        return ApiGatewayResponse.success(result);
    }

    /**
     * 高级版语音验证码，接受用户输入验证码
     */
    /*@RequestMapping(value = "/{account_id}/call/captcha_call",method = RequestMethod.POST)
    public ApiGatewayResponse captcha_call(HttpServletRequest request, @RequestBody CaptchaCallDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);

        //参数校验
        checkInputLen(dto.getFrom());
        if(StringUtils.isBlank(dto.getTo())){
            throw new RequestIllegalArgumentException();
        }
        if(dto.getMax_dial_duration() !=null && dto.getMax_dial_duration() <=0){
            throw new RequestIllegalArgumentException();
        }
        checkInputLen(dto.getVerify_code());
        if(dto.getMax_keys()!=null && dto.getMax_keys() <=0){
            throw new RequestIllegalArgumentException();
        }
        checkInputLen(dto.getUser_data());

        String callId = callService.captchaCall(ip,appId, dto);
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        return ApiGatewayResponse.success(result);
    }*/

//
//
//        @RequestMapping("/async/test")
//        @ResponseBody
//        public Callable<ApiGatewayResponse> callable(HttpServletResponse response) {
//            if(logger.isDebugEnabled()){
//                logger.debug("111111111");
//            }
//            // 这么做的好处避免web server的连接池被长期占用而引起性能问题，
//            // 调用后生成一个非web的服务线程来处理，增加web服务器的吞吐量。
//            return new Callable<ApiGatewayResponse>() {
//                @Override
//                public ApiGatewayResponse call() throws Exception {
//
//                    return ApiGatewayResponse.Success("中文2");
//                }
//            };
//        }


//    public static void main(String[] args) {
//        RestRequest restRequest = RestRequest.buildRequest();
//        Map<String,Object> hashMap =  new HashedMap();
//        hashMap.put("res_id","5566778");
//        ApiGatewayResponse<String> response = restRequest.post("http://www.yunhuni.cn:3000/incoming",hashMap);
//        System.out.println(response.getData());
//    }
}
