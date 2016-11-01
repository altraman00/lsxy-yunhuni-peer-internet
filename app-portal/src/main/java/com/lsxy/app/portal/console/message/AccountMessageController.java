package com.lsxy.app.portal.console.message;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.yunhuni.api.message.model.AccountMessage;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/4.
 */
@Controller
@RequestMapping("/console/message/account_message")
public class AccountMessageController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(AccountMessageController.class);
    /**
     * 获取未读消息
     * @return
     */
    @RequestMapping("/not/read")
    @ResponseBody
    private RestResponse countMessage(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/message/account_message/count";
        return  RestRequest.buildSecurityRequest(token).get(uri, Long.class);
    }
    /**
     * 用户消息首页
     * @param request
     * @param pageNo 第几页
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo,  @RequestParam(defaultValue = "20")Integer pageSize){
        edit(request);
        ModelAndView mav = new ModelAndView();
        RestResponse<Page<AccountMessage>> restResponse = plist(request,pageNo,pageSize);
        Page<AccountMessage> pageObj = restResponse.getData();
        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/message/index");
        return mav;
    }
    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/message/account_message/detail?id={1}";
        RestResponse restResponse = RestRequest.buildSecurityRequest(token).get(uri, AccountMessage.class,id);
        ModelAndView mav = new ModelAndView();
        mav.addObject("message",restResponse.getData());
        mav.setViewName("/console/message/detail");
        return  mav;
    }
    /**
     * 用户消息首页
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RestResponse list(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/message/account_message/list";
        return  RestRequest.buildSecurityRequest(token).getList(uri, AccountMessage.class);
    }
    /**
     * 修改状态为已读
     * @param request
     * @return
     */
    private RestResponse edit(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/message/account_message/edit";
        return  RestRequest.buildSecurityRequest(token).get(uri, Object.class);
    }
    /**
     * 查询用户下所有消息rest请求
     * @param request
     * @param pageNo 请求的页面
     * @param pageSize 每页多少条数据
     * @return
     */
    private RestResponse plist(HttpServletRequest request, Integer  pageNo, Integer pageSize){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/message/account_message/plist?pageNo={1}&pageSize={2}";
        return  RestRequest.buildSecurityRequest(token).getPage(uri, AccountMessage.class,pageNo,pageSize);
    }
    /**
     * 删除用户消息
     * @param request
     * @param id 用户消息id
     * @return
     */
    @RequestMapping("/delete")
    public ModelAndView delete(HttpServletRequest request,String id,@RequestParam(defaultValue = "1")Integer pageNo,@RequestParam(defaultValue = "20")Integer pageSize){
        deleteAccountMessage(request,id);
        ModelAndView mav = index(request,pageNo,pageSize);
        mav.addObject("msg","删除成功");
        return mav;
    }

    /**
     * 更新后台用户消息状态为已删除
     * @param request
     * @param id 用户消息删除
     * @return
     */
    private RestResponse deleteAccountMessage(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/message/account_message/delete";
        Map map = new HashMap();
        map.put("id",id);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, AccountMessage.class);
    }
}
