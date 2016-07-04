package com.lsxy.app.portal.console.telenum;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.console.account.InformationController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.resourceTelenum.model.ResourcesRent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 呼入号码管理
 * Created by zhangxb on 2016/6/30.
 */
@Controller
@RequestMapping("/console/telenum/callnum")
public class RentTelnumController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");

    /**
     * 呼入号码管理首页
     * @param request
     * @param pageNo 请求的页面
     * @param pageSize 每页多少条数据
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request,String pageNo, String pageSize){
        ModelAndView mav = new ModelAndView();
        RestResponse<Page<ResourcesRent>> restResponse = pageList(request,pageNo,pageSize);
        Page pageList= restResponse.getData();
        mav.addObject("pageList",pageList);
        mav.setViewName("/console/telenum/callnum/index");
        return mav;
    }

    /**
     * 查询租户下所有号码的rest请求
     * @param request
     * @param pageNo 请求的页面
     * @param pageSize 每页多少条数据
     * @return
     */
    private RestResponse pageList(HttpServletRequest request,String  pageNo, String pageSize){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/res_ret/list";
        Map map = new HashMap();
        int tempPageNo = validateNum(1,pageNo);
        int tempPageSize = validateNum(20,pageSize);//每页显示 Page.DEFAULT_PAGE_SIZE 20
        map.put("pageNo",tempPageNo);
        map.put("pageSize",tempPageSize);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, Page.class);
    }

    /**
     * 处理分页数据
     * @param tempNum 初始值
     * @param temp 处理的参数
     * @return
     */
    private int validateNum(int tempNum, String temp){
        if(temp!=null){
            tempNum = Integer.valueOf(temp);
            if(tempNum<=0){
                tempNum=1;
            }
        }
        return tempNum;
    }
}
