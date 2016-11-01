package com.lsxy.app.portal.console.telenum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.MobileCodeUtils;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
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
public class TestNumBindController extends AbstractPortalController{
    private static final Logger logger = LoggerFactory.getLogger(TestNumBindController.class);
    private static final String testCallNumber = SystemConfig.getProperty("portal.test.call.number");
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
        RestResponse<List<TestNumBind>> restResponse = list(request);
        List<TestNumBind> testNumBindList = restResponse.getData();
        mav.addObject("testCallNumber",testCallNumber);
        mav.addObject("testNumBindList",testNumBindList);
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
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/test_num_bind/list";
        return  RestRequest.buildSecurityRequest(token).getList(uri, TestNumBind.class);
    }
    /**
     * 查看绑定手机号码是否已经存在
     * @param number
     * @return
     */
    @RequestMapping("/isExist" )
    @ResponseBody
    private RestResponse isExist(HttpServletRequest request,String number){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/test_num_bind/isExist?number={1}";
        return  RestRequest.buildSecurityRequest(token).get(uri, String.class,number);
    }
    /**
     * 绑定手机号码
     * @param request
      @param testNumBind
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map save(HttpServletRequest request,TestNumBind testNumBind){
        Map hs = new HashMap();
        RestResponse<TestNumBind> restResponse = saveNumber(request,testNumBind);
        TestNumBind testMobileBind = restResponse.getData();
        if("0020".equals(restResponse.getErrorCode())){
            hs.put("sucess", RESULT_FIAL);
            hs.put("msg",restResponse.getErrorMsg());
        }else{
            String status = IS_FALSE;
            if(testNumBind.getNumber().equals(testMobileBind.getNumber())){
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
     * 应用绑定号码更新
     * @param request
     * @param numbers 测试号码id集合
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/update_app_number")
    @ResponseBody
    public RestResponse updateAppNumber(HttpServletRequest request,String numbers, String appId){
        RestResponse response = updateAppNumberRest(request, numbers, appId);
        if(response.isSuccess()){
            return RestResponse.success();
        }else{
            return response;
        }
    }
    /**
     * 测试手机绑定号码
     * @param request
     * @param numbers 测试号码id集合
     * @param appId 应用id
     * @return
     */
    private RestResponse updateAppNumberRest(HttpServletRequest request,String numbers ,String appId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/test_num_bind/update_app_number?numbers={1}&appId={2}";
        return  RestRequest.buildSecurityRequest(token).get(uri, TestNumBind.class,numbers,appId);
    }
    /**
     * 测试手机绑定号码
     * @param request
     * @param testNumBind 测试绑定手机
     * @return
     */
    private RestResponse saveNumber(HttpServletRequest request,TestNumBind testNumBind){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/test_num_bind/save";
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(testNumBind,Map.class);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, TestNumBind.class);
    }

    /**
     * 解除手机号码绑定
     * @param request
     * @param number
     * @return
     */
    @RequestMapping("/disbind")
    @ResponseBody
    public Map disbind(HttpServletRequest request,String number){
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
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/test_num_bind/disbind";
        Map map = new HashMap();
        map.put("number",number);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, String.class);
    }
}
