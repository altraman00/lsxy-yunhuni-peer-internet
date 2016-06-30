package com.lsxy.app.portal.rest.console.account;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangxb on 2016/6/24.
 * 基本资料处理类
 */
@Controller
@RequestMapping("/console/account/information")
public class InformationController {
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.restful.url");

    /**
     * 基本资料首页入口
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        //String token = (String)request.getSession(false).getAttribute(SUBMISSION_TOKEN);
        String url = restPrefixUrl + "/rest/account/find_by_username";
        RestResponse<Account> restResponse = restResponseUtils(url,null, Account.class);
        Account account = restResponse.getData();
       InformationVo informationVo = new InformationVo(account);

        mav.addObject("informationVo",informationVo);
        mav.setViewName("/console/account/information/index");
        return mav;
    }

    /**
     * 基本资料修改入口
     * @param request
     * @param informationVo 基本资料VO对象
     * @return
     */
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request, InformationVo informationVo ){
        ModelAndView mav = new ModelAndView();
        //修改数据库数据
        String url =   "/rest/account/information/save_information";
        Map map = getAccountParams(informationVo);
        RestResponse<Account> restResponse = restResponseUtils(url,map, Account.class);
        Account account = restResponse.getData();

        if(account!=null) {
            informationVo = new InformationVo(account);
            request.getSession().setAttribute("InformationVo", informationVo);
            mav.addObject("informationVo", informationVo);
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("informationVo", informationVo);
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/information/index");
        return mav;
    }

    /**
     * 拼装修改信息为map集合
     * @param informationVo 基本资料类
     * @return
     */
    private Map getAccountParams(InformationVo informationVo ){
        Map map = new HashMap();
        map.put("phone",informationVo.getMobile());
        map.put("industry",informationVo.getIndustry());
        map.put("business",informationVo.getBusiness());
        map.put("url",informationVo.getUrl());
        map.put("province",informationVo.getProvince());
        map.put("city",informationVo.getCity());
        map.put("address",informationVo.getAddress());
        return map;
    }
    /**
     * restPOST请求
     * @param url 请求地址
     * @param map 请求参数
     * @param className 返回值类型
     * @return
     */
    private RestResponse restResponseUtils(String url,Map map,Class className){
        // 获取实名认证的状态
        String userName = "user001";
        //调resr接口
        String uri = restPrefixUrl + url;
        String token = "1234";
        if(map == null){
            map = new HashMap();
        }
        map.put("userName",userName);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, className);
    }

}
