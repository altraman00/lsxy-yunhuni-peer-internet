package com.lsxy.app.portal.console.telenum;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.app.portal.console.account.InformationController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.resourceTelenum.model.TestMobileBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2016/6/30.
 * 测试号码绑定处理类
 */
@Controller
@RequestMapping("/console/telenum/bind")
public class TestMobileBindController extends AbstractPortalController{
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    private static final String RESULT_SUCESS = "2";//处理结果-成功
    private static final String RESULT_FIAL = "-2";//处理结果-失败
    private static final String IS_TRUE = "1";//表示成功
    private static final String IS_FALSE = "-1";//表示失败
    /**
     * 测试号码绑定首页
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        RestResponse<List<TestMobileBind>> restResponse = list(request);
        List<TestMobileBind> testMobileBindList = restResponse.getData();
        mav.addObject("testMobileBindList",testMobileBindList);
        mav.setViewName("/console/telenum/bind/index");
        return mav;
    }

    /**
     * 获取租户下所有测试绑定号码
     * @param request
     * @return
     */
    private RestResponse list(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/test_mobile_bind/list";
        Map map = new HashMap();
        return  RestRequest.buildSecurityRequest(token).post(uri,map, List.class);
    }

    /**
     * 绑定手机号码
     * @param request
      @param number
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map save(HttpServletRequest request,String number){
        Map hs = new HashMap();
        RestResponse<TestMobileBind> restResponse = saveNumber(request,number);
        TestMobileBind testMobileBind = restResponse.getData();
        if("0020".equals(restResponse.getErrorCode())){
            hs.put("sucess", RESULT_FIAL);
            hs.put("msg",restResponse.getErrorMsg());
        }else{
            String status = IS_FALSE;
            if(number.equals(testMobileBind.getNumber())){
                status = IS_TRUE;
            }
            if(IS_TRUE.equals(status)) {
                hs.put("sucess", RESULT_SUCESS);
                hs.put("msg", "新手机绑定成功！");
            }else{
                hs.put("sucess", RESULT_FIAL);
                hs.put("msg", "新手机绑定失败！");
            }
        }
        //将手机验证码删除
        MobileCodeUtils.removeMobileCodeChecker(request);
        return hs;
    }

    /**
     * 测试手机绑定号码
     * @param request
     * @param number 测试绑定手机
     * @return
     */
    private RestResponse saveNumber(HttpServletRequest request,String number){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/test_mobile_bind/save";
        Map map = new HashMap();
        map.put("number",number);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, TestMobileBind.class);
    }

    /**
     * 解除手机号码绑定
     * @param request
     * @param number
     * @return
     */
    @RequestMapping("/disbind")
    @ResponseBody
    public Map delete(HttpServletRequest request,String number){
        Map hs = new HashMap();
        RestResponse<String> restResponse = disbindNumber(request,number);
        String deleteNumber = restResponse.getData();
        hs.put("sucess", RESULT_SUCESS);
        hs.put("msg", "手机号码解除成功！");
        MobileCodeUtils.removeMobileCodeChecker(request);
        return hs;
    }
    /**
     * 解除手机号码绑定rest
     * @param request
     * @param number 测试绑定手机
     * @return
     */
    private RestResponse disbindNumber(HttpServletRequest request,String number){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/test_mobile_bind/disbind";
        Map map = new HashMap();
        map.put("number",number);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, String.class);
    }
}
