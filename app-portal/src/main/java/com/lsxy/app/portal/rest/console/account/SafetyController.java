package com.lsxy.app.portal.rest.console.account;

import com.lsxy.app.portal.rest.comm.MobileCodeUtils;
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
public class SafetyController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);

    private static final String IS_ERROR = "-2";//表示密码错误
    private static final String IS_FALSE = "-1";//表示失败
    private static final String IS_TRUE = "1";//表示成功
    private static final String RESULT_SUCESS = "2";//处理结果-成功
    private static final String RESULT_FIAL = "-2";//处理结果-失败
    //配置rest请求地址
    private String restPrefixUrl = SystemConfig.getProperty("portal.restful.url");
    /**
     * 安全设置首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        String userName = "user001";//redisCacheService.get("userName");
        //调resr接口
        String url = restPrefixUrl + "/rest/account/findByUserName";
        String token = "1234";
        Map map = new HashMap();
        map.put("userName",userName);
        RestResponse<Account> restResponse = RestRequest.buildSecurityRequest(token).post(url,map, Account.class);
        Account account = restResponse.getData();


        /*SafetyVo safetyVo = (SafetyVo) request.getSession().getAttribute("safetyVo");
        if(safetyVo == null) {
            safetyVo = new SafetyVo("-1",account.getUserName(), "1", account.getId(), new Date(), account.getMobile(),"1","-1","1");
        }*/
        SafetyVo safetyVo = new SafetyVo(account);

        request.getSession().setAttribute("safetyVo",safetyVo);
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
    public ModelAndView editPsw( String oldPassword,String newPassword){
        ModelAndView mav = new ModelAndView();
        //TODO 从redis中取或者从session取
        String userName = "user001";
        //调resr接口
        String url = restPrefixUrl + "/rest/account/safety/editPssword";
        String token = "1234";
        Map map = new HashMap();
        map.put("userName",userName);
        map.put("oldPassword",oldPassword);
        map.put("newPassword",newPassword);
        RestResponse<String> restResponse = RestRequest.buildSecurityRequest(token).post(url,map, String.class);

        String status = restResponse.getData();
        //TODO 0修改成功 -1表示失败
        if(IS_TRUE.equals(status) ){
            mav.addObject("msg", "修改成功！");
        }else if(IS_ERROR.equals(status)){
            mav.addObject("msg", "密码错误！");
        }else{
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }

    /**
     * 验证密码方法
     * @param oldPws 用户输入的密码
     * @return
     */
    @RequestMapping(value="/validation_psw" ,method = RequestMethod.POST)
    @ResponseBody
    public Map validationPsw(String oldPws ){
       HashMap hs = new HashMap();
        //TODO 从redis中取或者从session取
        String userName = "user001";
        //调resr接口
        String url = restPrefixUrl + "/rest/account/safety/validationPsw";
        String token = "1234";
        Map map = new HashMap();
        map.put("userName",userName);
        map.put("password",oldPws);
        RestResponse<String> restResponse = RestRequest.buildSecurityRequest(token).post(url,map, String.class);
        String result = restResponse.getData();
        //验证密码
        if(IS_TRUE.equals(result)) {
            hs.put("sucess", RESULT_SUCESS);
            hs.put("msg", "密码验证通过！");
        }else {
            hs.put("sucess", RESULT_FIAL);
            hs.put("msg", "密码验证失败！");
        }
        return hs;
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

        //TODO 从redis中取或者从session取
        String userName = "user001";
        //调resr接口
        String url = restPrefixUrl + "/rest/account/safety/updateMobile";
        String token = "1234";
        Map map = new HashMap();
        map.put("userName",userName);
        map.put("mobile",mobile);
        RestResponse<Account> restResponse = RestRequest.buildSecurityRequest(token).post(url,map, Account.class);
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


}
