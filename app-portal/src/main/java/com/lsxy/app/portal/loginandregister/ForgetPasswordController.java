package com.lsxy.app.portal.loginandregister;

import com.lsxy.app.portal.comm.MobileCodeChecker;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/6/24.
 * 忘记密码
 */
@Controller
@RequestMapping("/forget")
public class ForgetPasswordController {

    private static String ALLOW_RESET_PASSWORD = "allowResetPassword";
    private static String RESET_EMAIL_KEY = "resetEmailKey";
    private static String RESET_EMAIL = "resetEmail";
    private static String RESET_MOBILE = "resetMobile";
    private static String RESET_TYPE = "resetType";

    @Autowired
    private RedisCacheService cacheManager;


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView registerPage(){
        return new ModelAndView("forget/index");
    }

    @RequestMapping(value = "/check_mail",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse checkEmail(String email){
        Boolean b = emailCheck(email);
        return RestResponse.success(b);
    }

    @RequestMapping(value = "/send_mail",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView sendMail(String email){
        String returnView;
        Map<String,String> model = new HashMap();
        if(emailCheck(email)){
            //发送邮件
            sendEmail(email);
            //返回发送邮件成功的页面
            model.put("email",email);
            returnView = "forget/mail_success";
        }else{
            model.put("erInfo","该账户不存在");
            returnView = "forget/reset_fail";
        }
        return new ModelAndView(returnView,model);
    }

    @RequestMapping(value = "/check_mobile",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> checkMobile(String mobile){
        Map<String,Object> model = new HashMap();
        //检查手机是否有效
        if(mobileCheck(mobile)){
            model.put("flag",true);
        }else{
            model.put("flag",false);
            model.put("err","该账户不存在");
        }
        return model;
    }

    @RequestMapping(value = "/reset_pwd_mail",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView resetPasswordByEmail(HttpServletRequest request,String key,String code) {
        Map<String, String> model = new HashMap<>();
        //判断key是否有效，并查出与之关联的邮箱，一个key只有一次有效性
        String re = cacheManager.get(key);
        Map<String,String> map = JSONUtil.parseObject(re);
        if (code.equals(map.get("code"))) {//验证成功，进行邮箱修改
            if (StringUtils.isNotBlank(map.get("email"))) {
                request.getSession().setAttribute(ALLOW_RESET_PASSWORD, true);
                request.getSession().setAttribute(RESET_EMAIL,map.get("email"));
                request.getSession().setAttribute(RESET_EMAIL_KEY, key);
                model.put(RESET_TYPE, "email");
                return new ModelAndView("forget/reset_password", model);
            } else {
                model.put("erInfo", "无效的链接，或链接已过期");
                return new ModelAndView("forget/reset_fail", model);
            }
        }else{
            model.put("erInfo", "无效的链接，或链接已过期");
            return new ModelAndView("forget/reset_fail", model);
        }
    }


    @RequestMapping(value = "/reset_pwd_mobile",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView resetPasswordByMobile(HttpServletRequest request){
        Map<String,String> model = new HashMap<>();
        MobileCodeChecker checker = MobileCodeUtils.getMobileCodeChecker(request);
        if(checker != null){
            if(checker.isPass()){
                request.getSession().setAttribute(ALLOW_RESET_PASSWORD,true);
                request.getSession().setAttribute(RESET_MOBILE,checker.getMobile());
                //将手机验证码删除
                MobileCodeUtils.removeMobileCodeChecker(request);
                model.put(RESET_TYPE,"mobile");
                return new ModelAndView("forget/reset_password",model);
            }else{
                model.put("erInfo","验证无效");
                return new ModelAndView("forget/reset_fail",model);
            }
        }else{
            model.put("erInfo","验证凭证已过期");
            return new ModelAndView("forget/reset_fail",model);
        }
    }

    @RequestMapping(value = "/reset_password",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView resetPassword(HttpServletRequest request,String resetType,String password){
        Map<String,String> model = new HashMap<>();
        String erInfoKey = "erInfo";
        boolean isReset = false;
        Object isAllow = request.getSession().getAttribute(ALLOW_RESET_PASSWORD);
        if(isAllow != null && (boolean)isAllow){
            if("email".equals(resetType)){
                String email = (String) request.getSession().getAttribute(RESET_EMAIL);
                if(StringUtils.isNotBlank(email)){
                    //调用根据邮箱重设密码微服务
                    resetPwdByEmail(email,password);
                    isReset = true;
                    Object emailKey = request.getSession().getAttribute(RESET_EMAIL_KEY);
                    if(emailKey != null && emailKey instanceof String){
                        cacheManager.del((String)emailKey);
                    }
                }else{
                    model.put(erInfoKey,"凭证已过期");
                }
            }else if("mobile".equals(resetType)){
                String mobile = (String) request.getSession().getAttribute(RESET_MOBILE);
                if(StringUtils.isNotBlank(mobile)){
                    //调用根据手机重设密码微服务
                    resetPwdByMobile(mobile,password);
                    isReset = true;
                }else{
                    model.put(erInfoKey,"凭证已过期");
                }
            }else{
                model.put(erInfoKey,"参数错误");
            }
        }else{
            model.put(erInfoKey,"页面已过期");
        }
        if(isReset){
            //设置完后将缓存删掉
            request.getSession().removeAttribute(ALLOW_RESET_PASSWORD);
            request.getSession().removeAttribute(RESET_MOBILE);
            request.getSession().removeAttribute(RESET_EMAIL);
            request.getSession().removeAttribute(RESET_EMAIL_KEY);
            return new ModelAndView("forget/reset_success",model);
        }else{
            return new ModelAndView("forget/reset_fail",model);
        }

    }



    /**
     * restApi调用，用户信息校验，邮箱是否存在
     * @param email 邮箱
     */
    private Boolean emailCheck(String email){
        //此处调用户信息检验RestApi
        String infoCheckUrl = PortalConstants.REST_PREFIX_URL + "/forget/check_email?email={1}";
        RestResponse<Boolean> response = RestRequest.buildRequest().get(infoCheckUrl,Boolean.class,email);
        return response.getData();
    }

    /**
     * restApi调用，用户信息校验，手机是否存在
     * @param mobile 手机
     */
    private Boolean mobileCheck(String mobile){
        //此处调用户信息检验RestApi
        String infoCheckUrl = PortalConstants.REST_PREFIX_URL + "/forget/check_mobile?mobile={1}";
        RestResponse<Boolean> response = RestRequest.buildRequest().get(infoCheckUrl,Boolean.class,mobile);
        return response.getData();
    }

    /**
     * restApi调用，发送修改密码邮件
     * @param email
     */
    private void sendEmail(String email){
        //此处调用户信息检验RestApi
        String sendEmail = PortalConstants.REST_PREFIX_URL + "/forget/send_email?email={1}";
        RestRequest.buildRequest().get(sendEmail,null,email);
    }

    /**
     * restApi调用，根据邮箱修改密码
     * @param email
     */
    private void resetPwdByEmail(String email,String password){
        //此处调根据邮箱修改密码RestApi
        String regInfoCheckUrl = PortalConstants.REST_PREFIX_URL + "/forget/reset_pwd_email?email={1}&password={2}";
        RestRequest.buildRequest().get(regInfoCheckUrl,null,email,password);
    }

    /**
     * restApi调用，根据手机修改密码
     * @param mobile
     */
    private void resetPwdByMobile(String mobile,String password){
        //此处调根据邮箱修改密码RestApi
        String regInfoCheckUrl = PortalConstants.REST_PREFIX_URL + "/forget/reset_pwd_mobile?mobile={1}&password={2}";
        RestRequest.buildRequest().get(regInfoCheckUrl,null,mobile,password);
    }

}
