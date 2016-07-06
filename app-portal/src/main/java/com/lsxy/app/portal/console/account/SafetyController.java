package com.lsxy.app.portal.console.account;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/6/24.
 * 安全设置
 */
@Controller
@RequestMapping("/console/account/safety")
public class SafetyController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);

    private static final String IS_ERROR = "-2";//表示密码错误
    private static final String IS_FALSE = "-1";//表示失败
    private static final String IS_TRUE = "1";//表示成功
    private static final String RESULT_SUCESS = "2";//处理结果-成功
    private static final String RESULT_FIAL = "-2";//处理结果-失败
    //配置rest请求地址
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    /**
     * 安全设置首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        RestResponse<Account> restResponse = findByUsername(request);
        Account account = restResponse.getData();
        SafetyVo safetyVo = new SafetyVo(account);
        request.getSession().setAttribute("safetyVo",safetyVo);
        mav.addObject("safetyVo",safetyVo);
        mav.setViewName("/console/account/safety/index");
        return mav;
    }

    /**
     * 获取用户信息的rest请求
     * @return
     */
    private RestResponse findByUsername(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +  "/rest/account/find_by_username";
        Map map = new HashMap();
        return  RestRequest.buildSecurityRequest(token).post(uri,map,  Account.class);
    }


    /**
     * 修改密码首页
     * @return
     */
    @RequestMapping(value="/index_psw" )
    public ModelAndView indexPsw(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }

    /**
     *  修改密码方法
     * @return
     */
    @RequestMapping(value="/edit_psw" ,method = RequestMethod.POST)
    public ModelAndView editPsw(HttpServletRequest request, String oldPassword,String newPassword){
        ModelAndView mav = new ModelAndView();
        RestResponse<String> restResponse = savePassword(request,oldPassword, newPassword);
        String status = restResponse.getData();
        //TODO 0修改成功 -1表示失败
        if(IS_TRUE.equals(status) ){
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("msg", restResponse.getErrorMsg());
        }
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }

    /**
     * 保存密码的rest请求
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    private RestResponse savePassword(HttpServletRequest request,String oldPassword,String newPassword){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/account/safety/save_password";
        Map map = new HashMap();
        map.put("oldPassword",oldPassword);
        map.put("newPassword",newPassword);
        return  RestRequest.buildSecurityRequest(token).post(uri,map,  String.class);
    }

    /**
     * 验证密码方法
     * @param oldPws 用户输入的密码
     * @return
     */
    @RequestMapping(value="/validation_psw" ,method = RequestMethod.POST)
    @ResponseBody
    public Map validationPsw(HttpServletRequest request,String oldPws ){
       HashMap hs = new HashMap();

        RestResponse<String> restResponse = validationPassword(request,oldPws);
        String result = restResponse.getData();
        //验证密码
        if(IS_TRUE.equals(result)) {
            hs.put("sucess", RESULT_SUCESS);
            hs.put("msg", "密码验证通过！");
        }else {
            hs.put("sucess", RESULT_FIAL);
            hs.put("msg", restResponse.getErrorMsg());
        }
        return hs;
    }

    /**
     * 验证密码的rest方法
     * @param oldPws
     * @return
     */
    private RestResponse validationPassword(HttpServletRequest request,String oldPws){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/account/safety/validation_password";
        Map map = new HashMap();
        map.put("password",oldPws);
        return  RestRequest.buildSecurityRequest(token).post(uri,map,  String.class);
    }

    /**
     *  绑定手机号码
     * @param mobile 新手机号码
     * @param request
     * @return
     */
    @RequestMapping(value="/edit_mobile" ,method = RequestMethod.POST)
    @ResponseBody
    public Map editMobile(String mobile ,HttpServletRequest request ){
        HashMap hs = new HashMap();
        RestResponse<Account> restResponse = saveMobile(request,mobile);
        Account account = restResponse.getData();
        String status = IS_FALSE;
        if(mobile.equals(account.getMobile())){
            status = IS_TRUE;
        }
        if(IS_TRUE.equals(status)) {
            SafetyVo safetyVo = new SafetyVo(account);
            request.getSession().setAttribute("safetyVo",safetyVo);
            //将手机验证码删除
            MobileCodeUtils.removeMobileCodeChecker(request);
            hs.put("sucess", RESULT_SUCESS);
            hs.put("msg", "新手机绑定成功！");
        }else{
            hs.put("sucess", RESULT_FIAL);
            hs.put("msg", "新手机绑定失败！");
        }

        return hs;
    }

    /**
     *  保存手机号码的方法
     * @param mobile 手机号码
     * @return
     */
    private RestResponse<Account> saveMobile(HttpServletRequest request,String mobile) {
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +  "/rest/account/safety/save_mobile";
        Map map = new HashMap();
        map.put("mobile",mobile);
        return  RestRequest.buildSecurityRequest(token).post(uri,map,  Account.class);
    }

}
