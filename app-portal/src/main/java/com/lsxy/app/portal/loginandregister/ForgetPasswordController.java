package com.lsxy.app.portal.loginandregister;

import com.lsxy.app.portal.comm.MobileCodeChecker;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liups on 2016/6/24.
 * 忘记密码
 */
@Controller
@RequestMapping("/forget")
public class ForgetPasswordController {

    private static String ALLOW_RESET_PASSWORD = "allowResetPassword";
    private static String RESET_EMAIL = "resetEmail";
    private static String RESET_MOBILE = "resetMobile";
    private static String RESET_TYPE = "resetType";

//    @Autowired
//    RedisCacheManager redisCacheManager;


    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView registerPage(){
        return new ModelAndView("forget/index");
    }

    @RequestMapping(value = "/send_mail",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView sendMail(String email){
        Map<String,String> model = new HashMap();
        //TODO 检查邮件地址是否有效
        boolean flag =  "123456@qq.com".equals(email);
        if(!flag){
            model.put("erInfo","无效的邮箱");
            return new ModelAndView("forget/reset_fail",model);
        }

        //TODO 发送邮件（参数是一个UUID），并将其存到数据库（redis?）
        String key = UUID.randomUUID().toString();

        //返回发送邮件成功的页面
        model.put("email",email);
        return new ModelAndView("forget/mail_success",model);
    }

    @RequestMapping(value = "/check_mobile",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> checkMobile(String mobile){
        Map<String,Object> model = new HashMap();
        //TODO 检查手机是否有效
        boolean flag = "13750001373".equals(mobile);
        if(flag){
            model.put("flag",true);
        }else{
            model.put("flag",false);
            model.put("err","无效的手机号");
        }
        return model;
    }

    @RequestMapping(value = "/reset_pwd_mail",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView resetPasswordByEmail(HttpServletRequest request,String key){
        Map<String,String> model = new HashMap<>();
        //TODO 判断key是否有效，并查出与之关联的邮箱，一个key只有一次有效性
        boolean flag = "123456".equals(key);
        String email = "123456@qq.com";
        if(flag){
            request.getSession().setAttribute(ALLOW_RESET_PASSWORD,true);
            request.getSession().setAttribute(RESET_EMAIL,email);
            model.put(RESET_TYPE,"email");
            return new ModelAndView("forget/reset_password",model);
        }else{
            model.put("erInfo","无效的链接，或链接已过期");
            return new ModelAndView("forget/reset_fail",model);
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
        boolean isAllow = (boolean) request.getSession().getAttribute(ALLOW_RESET_PASSWORD);
        if(isAllow){
            if("email".equals(resetType)){
                String email = (String) request.getSession().getAttribute(RESET_EMAIL);
                if(!StringUtils.isEmpty(email)){
                    //TODO 调用根据邮箱重设密码微服务

                    isReset = true;

                }else{
                    model.put(erInfoKey,"凭证已过期");
                }
            }else if("mobile".equals(resetType)){
                String mobile = (String) request.getSession().getAttribute(RESET_MOBILE);
                if(!StringUtils.isEmpty(mobile)){
                    //TODO 调用根据手机重设密码微服务

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
            return new ModelAndView("forget/reset_success",model);
        }else{
            return new ModelAndView("forget/reset_fail",model);
        }

    }

}
