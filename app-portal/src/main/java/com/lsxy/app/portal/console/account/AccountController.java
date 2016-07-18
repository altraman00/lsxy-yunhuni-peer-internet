package com.lsxy.app.portal.console.account;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
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
 * 基本资料
 * Created by Tandy on 2016/6/8.
 */
@Controller
@RequestMapping("/console/account")
public class AccountController  extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    /**
     * 基本资料首页入口
     * @param request
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.addObject("account",getCurrentAccount(request));
        mav.setViewName("/console/account/information/index");
        return mav;
    }

    /**
     * 基本资料修改入口
     * @param request
     * @return
     */
    @RequestMapping(value="/update" ,method = RequestMethod.POST)
    @ResponseBody
    public Map update(HttpServletRequest request){
        Map<String,Object> paramsMap = WebUtils.getRequestParams(request);
        updateAccont(request,paramsMap);
        Map map = new HashMap();
        map.put("msg","修改资料成功");
        return map;
    }

    /**
     * 保存基本资料的rest请求
     * @return
     */
    private RestResponse updateAccont( HttpServletRequest request, Map<String,Object> paramsMap ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/account/update";
        return  RestRequest.buildSecurityRequest(token).post(uri,paramsMap, Account.class);
    }

}
