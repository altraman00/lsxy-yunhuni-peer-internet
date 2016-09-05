package com.lsxy.app.portal.loginandregister;

import com.lsxy.app.portal.comm.MobileCodeChecker;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import com.lsxy.framework.api.exceptions.RegisterException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by liups on 2016/6/20.
 * 用户注册
 */
@Controller
@RequestMapping("/reg")
public class RegisterController {
    public static final String ACCOUNT_MACTIVE_PREFIX = "account_mactive_";

    @Autowired
    private RedisCacheService cacheManager;

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView registerPage(){
        return new ModelAndView("register/index");
    }

    /**
     * 检查注册信息是否已被使用
     */
    @RequestMapping(value = "/info_check" , method = RequestMethod.POST)
    @ResponseBody
    public Map infoCheck(String userName,  String mobile, String email){
        Map<String,Object> result = new HashMap<>();
        result.put("flag",true);
        try {
            //此处调用用户注册信息验证
            regInfoCheck(userName,mobile,email);
        } catch (RegisterException e) {
            //提示信息：验证没通过
            result.put("err",e.getMessage());
            result.put("flag",false);
        }
        return result;
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
//    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView register(HttpServletRequest request, String userName, String mobile,String email){
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            String xxx = request.getParameter("userName");
            System.out.println(xxx);
            String xx = new String(userName.getBytes("iso-8859-1"),"UTF-8");
            System.out.println(xx);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebUtils.logRequestParams(request);

        return null;
//        Map<String,String> model = new HashMap<>();
//        String erInfo = "erInfo";
//        String erPage = "register/fail";
//        String successPage = "register/success";
//        //在此再进行一次用户名的正则校验（因为有些非法用户名会导致账号异常，如手机号，邮箱）
//        if(StringUtils.isNotBlank(userName)){
//            String regEx="^[0-9]*$|[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
//            Pattern p = Pattern.compile(regEx);
//            boolean m = p.matcher(userName).find();
//            if(m){
//                throw new RuntimeException("非法用户名");
//            }
//        }else{
//            throw new RuntimeException("用户名为空");
//        }
//        //获取手机验证码
//        MobileCodeChecker checker = MobileCodeUtils.getMobileCodeChecker(request);
//        if(checker != null){
//            if(checker.getMobile().equals(mobile) && checker.isPass()){
//                try {
//                    //调用创建账号方法
//                    createAccount(userName,mobile,email);
//                } catch (RegisterException e) {
//                    model.put(erInfo,e.getMessage());
//                    return new ModelAndView(erPage,model);
//                }
//                //将手机验证码删除
//                MobileCodeUtils.removeMobileCodeChecker(request);
//
//                //返回页面，通知查收邮件激活账号
//                model.put("email",email);
//                return new ModelAndView(successPage,model);
//            }else{
//                //提示信息：手机验证没通过
//                model.put(erInfo,"手机验证没通过");
//                return new ModelAndView(erPage,model);
//            }
//        }else{
//            //提示信息：手机验证码过期
//            model.put(erInfo,"手机验证码过期");
//            return new ModelAndView(erPage,model);
//        }

    }

    /**
     * 用户激活，跳转到用户密码设置页面
     */
    @RequestMapping(value = "/mail_active",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView mailActive(String uid,String code){
        Map<String,String> model = new HashMap<>();
        String returnView;
        //检查邮件是否有效
        String cCode = cacheManager.get(ACCOUNT_MACTIVE_PREFIX + uid);
        Account account = getAccount(uid);
        if(account == null){
            model.put("erInfo","参数异常");
            returnView = "register/active_fail";
        }else{
            if(code.equals(cCode)){
                if(Account.STATUS_NOT_ACTIVE == account.getStatus()){
                    //没有激活,前往激活页面
                    model.put("uid",uid);
                    model.put("username",account.getUserName());
                    model.put("code",code);
                    returnView = "register/active";
                }else if(Account.STATUS_NORMAL == account.getStatus()){
                    //已经激活
                    model.put("info","账户已经激活");
                    returnView = "register/active_result";
                }else{
                    model.put("erInfo","账号已过期");
                    returnView = "register/active_fail";
                }
            }else{
                //检查用户是否激活
                if(account.getStatus() == Account.STATUS_NORMAL){
                    model.put("info","账户已经激活");
                    returnView = "register/active_result";
                }else{
                    model.put("erInfo","参数异常或邮件已过期");
                    returnView = "register/active_fail";
                }
            }
        }
        return new ModelAndView(returnView,model);
    }

    @RequestMapping(value = "/active",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView active(String uid,String code,String password){
        String returnUrl;
        Map<String,String> model = new HashMap<>();
        //判断用户激活条件是否合格
        String cCode = cacheManager.get(ACCOUNT_MACTIVE_PREFIX + uid);
        if(StringUtils.isNotBlank(code)&&code.equals(cCode)){
            try {
                //激活账号
                activeAccount(uid,password);
                model.put("info","账户已经激活");
                returnUrl = "register/active_result";
                //激活后删除redis里的邮件标识
                cacheManager.del(ACCOUNT_MACTIVE_PREFIX + uid);
            } catch (RegisterException e) {
                model.put("erInfo",e.getMessage());
                returnUrl = "register/active_fail";
            }
        }else{
            model.put("erInfo","激活失败：参数异常或激活时间已过期");
            returnUrl = "register/active_fail";
        }
        return new ModelAndView(returnUrl,model);
    }


    /**
     * 用户注册信息校验
     * @param userName 用户名
     * @param mobile 手机
     * @param email 邮箱
     */
    private void regInfoCheck(String userName,String mobile,String email){
        //此处调用户注册信息检验RestApi
        String regInfoCheckUrl = PortalConstants.REST_PREFIX_URL + "/reg/reg_info_check?userName={1}&mobile={2}&email={3}";
        RestResponse<Integer> response = RestRequest.buildRequest().get(regInfoCheckUrl,Integer.class,userName,mobile,email);
        //如果不成功，则抛出注册异常
        if(!response.isSuccess()){
            throw new RegisterException(response.getErrorMsg());
        }
    }

    /**
     * 用户激活
     * @param accountId 账号ID
     * @param password 密码
     */
    private void activeAccount(String accountId,String password){
        //此处调用户激活信息检验RestApi
        String regInfoCheckUrl = PortalConstants.REST_PREFIX_URL + "/reg/active?accountId={1}&password={3}";
        RestResponse<Account> response = RestRequest.buildRequest().get(regInfoCheckUrl,Account.class,accountId,password);
        //如果不成功，则抛出注册异常
        if(!response.isSuccess()){
            throw new RegisterException(response.getErrorMsg());
        }
    }

    /**
     * 创建账号
     * @param userName 用户名
     * @param mobile 手机
     * @param email 邮箱
     */
    private void createAccount(String userName,String mobile,String email){
        //此处调用创建账号RestApi
        String createAccountUrl = PortalConstants.REST_PREFIX_URL + "/reg/create_account?userName={1}&mobile={2}&email={3}";
        RestResponse<Account> response = RestRequest.buildRequest().get(createAccountUrl,Account.class,userName,mobile,email);
        //如果不成功，则抛出注册异常
        if(!response.isSuccess()){
            throw new RegisterException(response.getErrorMsg());
        }
    }

    /**
     * 获取账号状态
     * @return
     */
    private Account getAccount(String accountId){
        //此处调用获取账号状态的RestApi接口
        String createAccountUrl = PortalConstants.REST_PREFIX_URL + "/reg/account/{1}";
        RestResponse<Account> response = RestRequest.buildRequest().get(createAccountUrl,Account.class,accountId);
        return response.getData();
    }

}
