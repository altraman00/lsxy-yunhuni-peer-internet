package com.lsxy.app.portal.console.account;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by zhangxb on 2016/6/24.
 * 安全设置
 */
@Controller
@RequestMapping("/console/account/safety")
public class SafetyController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);
    @Autowired
    private RedisCacheService cacheManager;
    private static final String IS_ERROR = "-2";//表示密码错误
    private static final String IS_FALSE = "-1";//表示失败
    private static final String IS_TRUE = "1";//表示成功
    private static final String RESULT_SUCESS = "2";//处理结果-成功
    private static final String RESULT_FIAL = "-2";//处理结果-失败
    /**
     * 安全设置首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        Account accoutn  =getCurrentAccount(request);
        SafetyVo safetyVo = new SafetyVo(accoutn);
        String key = "account_modify_email_"+accoutn.getId();
        String re = cacheManager.get(key);
        if(!StringUtils.isEmpty(re)){
            Map map = JSONUtil.parseObject(re);
            mav.addObject("modifyEmail",map.get("email"));
            safetyVo.setIsEmail("0");
        }
        mav.addObject("safetyVo",safetyVo);
        mav.setViewName("/console/account/safety/index");
        return mav;
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
    @ResponseBody
    public Map editPsw(HttpServletRequest request, String oldPassword,String newPassword){
        ModelAndView mav = new ModelAndView();
        RestResponse<String> restResponse = modifyPwd(request,oldPassword, newPassword);
        String status = restResponse.getData();
        Map map = new HashMap();
        if(IS_TRUE.equals(status) ){
            map.put("code","0");
            map.put("msg", "修改成功！");
        }else{
            map.put("code","1");
            map.put("msg", restResponse.getErrorMsg());
        }
        return map;
    }

    /**
     * 保存密码的rest请求
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return
     */
    private RestResponse modifyPwd(HttpServletRequest request,String oldPassword,String newPassword){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/account/safety/modify_pwd";
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
    @RequestMapping(value="/modify_email_bind")
    @ResponseBody
    public RestResponse modifyEmailBind(HttpServletRequest request,String email ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/account/safety/modify_email_bind?email={1}";
        RestResponse restResponse = RestRequest.buildSecurityRequest(token).get(uri,  String.class,email);
        return restResponse;
    }

    /**
     * 验证密码的rest方法
     * @param oldPws
     * @return
     */
    private RestResponse validationPassword(HttpServletRequest request,String oldPws){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/account/safety/validation_password?password={1}";
        return  RestRequest.buildSecurityRequest(token).get(uri,  String.class,oldPws);
    }

    /**
     *  绑定手机号码
     * @param mobile 新手机号码
     * @param request
     * @return
     */
    @RequestMapping(value="/edit_mobile" ,method = RequestMethod.POST)
    @ResponseBody
    public RestResponse editMobile(String mobile ,HttpServletRequest request ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +  "/rest/account/safety/save_mobile?mobile={1}";
        RestResponse restResponse =  RestRequest.buildSecurityRequest(token).get(uri,Account.class,mobile);
        if(restResponse.isSuccess()) {
            MobileCodeUtils.removeMobileCodeChecker(request);
        }
        return restResponse;
    }

}
