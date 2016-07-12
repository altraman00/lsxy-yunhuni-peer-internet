package com.lsxy.app.portal.comm;

import com.lsxy.app.portal.utils.InternetProtocolUtil;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/6/22.
 */
@RequestMapping("/mc")
@Controller
public class MCController {
    public static final String IP_CODE_PREFIX = "ip_code_";

    @Autowired
    RedisCacheService redisCacheService;
    /**
     * 发送手机验证码
     */
    @RequestMapping(value = "/send",method = RequestMethod.GET)
    @ResponseBody
    public Map send(HttpServletRequest request,String mobile,String validateCode){
        Map<String,Object> model = new HashMap<>();
        if(StringUtils.isNotBlank(mobile)){
            //TODO 从配置文件里取出各个配置
            Long expire = 30 * 60L;
            Integer maxNum = 3;
            String remoteAddr = InternetProtocolUtil.getRemoteAddr(request);
            if(StringUtils.isNotBlank(remoteAddr)){
                String key = IP_CODE_PREFIX + remoteAddr;
                //获取缓存里的IP请求的次数，并加1
                long num = redisCacheService.incr(key);
                //将有效时间设置为规定的时间
                redisCacheService.expire(key,expire);
                //是否发送验证码
                boolean sendMC;
                if(num > maxNum){
                    //检验图形验证码
                    String expect = (String) request.getSession().getAttribute(PortalConstants.VC_KEY);
                    if(StringUtils.isBlank(expect)){
                        //没有，若没有图形验证码，返回需要图入图形认证码的提示
                        model.put("flag",false); //认证错误
                        model.put("vc",true);  //下一次输入要输入图形认证码
                        model.put("err","请输入图形验证码"); //错误信息
                        sendMC = false;
                    }else if(!expect.equalsIgnoreCase(validateCode)){
                        //错误，图形验证码一次验证不过就清空
                        request.getSession().removeAttribute(PortalConstants.VC_KEY);
                        model.put("flag",false);
                        model.put("vc",true);
                        if(StringUtils.isBlank(validateCode)){
                            model.put("err","请输入图形验证码");
                        }else{
                            model.put("err","图形认证码错误");
                        }
                        sendMC = false;
                    }else{
                        //正确，清空图形验证码
                        request.getSession().removeAttribute(PortalConstants.VC_KEY);
                        model.put("flag",true); //认证通过
                        sendMC = true;
                    }
                }else{
                    model.put("flag",true); //认证通过
                    sendMC = true;
                }
                if(sendMC){
                    //发送手机验证码
                    sendMobileCode(mobile);
                }
            }else{
                model.put("flag",false);
                model.put("err","IP异常");
            }
        }else{
            model.put("flag",false);
            model.put("err","请输入手机号");
        }
        return model;
    }

    /**
     * 验证手机验证码
     */
    @RequestMapping(value = "/check",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> checkMobileCode(HttpServletRequest request, String mobile, String mc){
        Map<String,Object> result = new HashMap<>();
        //检查手机验证码
        if(StringUtils.isNotBlank(mc) && StringUtils.isNotBlank(mobile)){
            String s = checkMobileCode(mobile, mc);
            if("1".equals(s)){
                MobileCodeChecker checker = new MobileCodeChecker(mobile,true);
                MobileCodeUtils.setMobileCodeChecker(request,checker);
                result.put("flag",true);
                result.put("msg","验证通过");
            }else{
                result.put("flag",false);
                result.put("err",s);
            }
            return result;
        }else{
            result.put("flag",false);
            result.put("err","手机号或验证码为空");
            return result;
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
