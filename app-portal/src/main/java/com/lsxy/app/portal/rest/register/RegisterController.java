package com.lsxy.app.portal.rest.register;

import com.lsxy.app.portal.rest.comm.MobileCodeChecker;
import com.lsxy.app.portal.rest.comm.MobileCodeUtils;
import com.lsxy.app.portal.rest.exceptions.RegisterException;
import com.lsxy.app.portal.rest.security.AvoidDuplicateSubmission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/6/20.
 * 用户注册
 */
@Controller
@RequestMapping("/reg")
public class RegisterController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView registerPage(){
        return new ModelAndView("register/index");
    }

    /**
     * 检查注册信息是否已被使用
     */
    @RequestMapping(value = "/info_check" , method = RequestMethod.GET)
    @ResponseBody
    public Map infoCheck(@Nonnull String username, @Nonnull String mobile,@Nonnull String email){
        Map<String,Object> result = new HashMap<>();
        result.put("flag",true);
        try {
            //TODO 此处调用用户注册验证微服务
            regInfoCheck(username,mobile,email);
        } catch (RegisterException e) {
            //提示信息：手机验证没通过
            result.put("err",e.getMessage());
            result.put("flag",false);
        }
        return result;
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView register(HttpServletRequest request,@Nonnull String username, @Nonnull String mobile,@Nonnull String email){
        Map<String,String> model = new HashMap<>();
        String erInfo = "erInfo";
        String erPage = "register/fail";
        String successPage = "register/success";
        //获取手机验证码
        MobileCodeChecker checker = MobileCodeUtils.getMobileCodeChecker(request);
        if(checker != null){
            if(checker.getMobile().equals(mobile) && checker.isPass()){
                try {
                    //TODO 此处调用用户注册验证微服务
                    regInfoCheck(username,mobile,email);
                } catch (RegisterException e) {
                    model.put(erInfo,e.getMessage());
                    return new ModelAndView(erPage,model);
                }

                //TODO 所有信息验证通过，保存用户注册信息并发邮件通知

                //将手机验证码删除
                MobileCodeUtils.removeMobileCodeChecker(request);

                //返回页面，通知查收邮件激活账号
                model.put("email",email);
                return new ModelAndView(successPage,model);
            }else{
                //提示信息：手机验证没通过
                model.put(erInfo,"手机验证没通过");
                return new ModelAndView(erPage,model);
            }
        }else{
            //提示信息：手机验证码过期
            model.put(erInfo,"手机验证码过期");
            return new ModelAndView(erPage,model);
        }

    }

    /**
     * 用户激活，跳转到用户密码设置页面
     */
    @RequestMapping(value = "/mail_active",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView mailActive(String uid,String username,String code){
        Map<String,String> model = new HashMap<>();
        //TODO 用户是否已激活
        boolean isActive = "12345678".equals(uid);//模拟激活
        if(isActive){
            //已经激活
            model.put("info","该账户已经激活");
            return new ModelAndView("register/active_result",model);
        }else{
            //没有激活,前往激活页面
            model.put("uid",uid);
            model.put("username",username);
            model.put("code",code);
            return new ModelAndView("register/active",model);
        }
    }

    @RequestMapping(value = "/active",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView active(String uid,String code,String password){
        Map<String,String> model = new HashMap<>();
        //TODO 判断用户激活条件是否合格

        //TODO 修改激活状态

        //TODO 创建主鉴权账号

        //TODO 帐务数据创建

        //返回激活结果页面
        boolean isActive = "123456789".equals(uid)&&"123456".equals(code);//模拟各个条件
        if(isActive){
            model.put("info","激活成功");
            return new ModelAndView("register/active_result",model);
        }else{
            model.put("info","激活失败：某些原因");
            return new ModelAndView("register/active_result",model);
        }
    }


    //模拟微服务校验
    private static void regInfoCheck(String username,String mobile,String email){
        if(!regInfoCheck("username", username)){
            //提示信息：用户名已被注册
            throw new RegisterException("注册失败，用户名已被注册");
        }
        if(!regInfoCheck("mobile",mobile)){
            //提示信息：手机已被注册
            throw new RegisterException("注册失败，手机号已被注册");
        }
        if(!regInfoCheck("email",email)){
            //提示信息：邮箱已被注册
            throw new RegisterException("注册失败，邮箱已被注册");
        }
    }

    //模拟微服务校验
    private static boolean regInfoCheck(String type,String data){
        if("username".equals(type)){
            return "username".equals(data);
        }else if("mobile".equals(type)){
            return "13750001373".equals(data);
        }else if("email".equals(type)){
            return "123456@qq.com".equals(data);
        }else{
            return false;
        }
    }

}
