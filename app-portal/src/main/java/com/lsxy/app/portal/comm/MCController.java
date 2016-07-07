package com.lsxy.app.portal.comm;

import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/6/22.
 */
@RequestMapping("/mc")
@Controller
public class MCController {
    /**
     * 发送手机验证码
     */
    @RequestMapping(value = "/send",method = RequestMethod.GET)
    @ResponseBody
    public void getMobileCode(HttpServletRequest request,String mobile){
        if(StringUtils.isNotBlank(mobile)){
            sendMobileCode(mobile);
        }
    }

    /**
     * 验证手机验证码 成功返回1 ，失败则返回原因
     */
    @RequestMapping(value = "/check",method = RequestMethod.GET)
    @ResponseBody
    public String checkMobileCode(HttpServletRequest request, String mobile,String mc){
        //检查手机验证码
        if(StringUtils.isNotBlank(mc) && StringUtils.isNotBlank(mobile)){
            String s = checkMobileCode(mobile, mc);
            if("1".equals(s)){
                MobileCodeChecker checker = new MobileCodeChecker(mobile,true);
                MobileCodeUtils.setMobileCodeChecker(request,checker);
            }
            return s;
        }else{
            return "手机号或验证码为空";
        }
    }

    /**
     * restApi发送短信验证码
     * @param mobile
     */
    private void sendMobileCode(String mobile){
        //此处调用发送短信验证码RestApi
        String sendUrl = PortalConstants.REST_PREFIX_URL + "/code/send_mobile_code?mobile={1}";
        RestRequest.buildRequest().get(sendUrl,null,mobile);
    }

    /**
     * restApi校验手机验证码 成功返回1 ，失败则返回原因
     * @param mobile
     */
    private String checkMobileCode(String mobile,String code){
        //此处调用检验短信验证码RestApi
        String checkUrl = PortalConstants.REST_PREFIX_URL + "/code/check_mobile_code?mobile={1}&code={2}";
        RestResponse<Boolean> result = RestRequest.buildRequest().get(checkUrl, Boolean.class, mobile, code);
        if(result.isSuccess()){
            return "1";
        }else{
            return result.getErrorMsg();
        }
    }

}
