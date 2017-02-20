package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.console.app.vo.SubAccountVo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by zhangxb on 2017/2/16.
 */
@Controller
@RequestMapping("/console/sub_account")
public class SubAccountController extends AbstractPortalController {
    private Logger logger = LoggerFactory.getLogger(SubAccountController.class);
    @RequestMapping(value = "/new")
    @ResponseBody
    public RestResponse create(HttpServletRequest request,String appId, String url, int seatNum, int voiceNum, String remark){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/new";
        Map<String,Object> map = new HashedMap(){{
            put("url",url);
            put("appId",appId);
            put("seatNum",seatNum);
            put("voiceNum",voiceNum);
            put("remark",remark);
        }};
        RestResponse response =  RestRequest.buildSecurityRequest(token).post(uri,map);
        return response;
    }
    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public RestResponse findById(HttpServletRequest request,@PathVariable String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/detail/"+id;
        RestResponse response =  RestRequest.buildSecurityRequest(token).get(uri,SubAccountVo.class);
        return response;
    }
    @RequestMapping(value = "/edit")
    @ResponseBody
    public RestResponse edit(HttpServletRequest request,String id, String url, int seatNum, int voiceNum, String remark){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/edit/"+id;
        Map<String,Object> map = new HashedMap(){{
            put("url",url);
            put("seatNum",seatNum);
            put("voiceNum",voiceNum);
            put("remark",remark);
        }};
        RestResponse response =  RestRequest.buildSecurityRequest(token).post(uri,map);
        return response;
    }
    @RequestMapping(value = "/list")
    @ResponseBody
    public RestResponse list(HttpServletRequest request,String appId,Integer pageNo,Integer pageSize, String certId, String remark, Integer enabled){
        String token = getSecurityToken(request);//  ?pageNo={1}&pageSize={2}
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/list/"+appId+"?pageNo={1}&pageSize={2}&certId={3}&remark={4}&enabled={5}";
        RestResponse<Page<SubAccountVo>> response =  RestRequest.buildSecurityRequest(token).getPage(uri, SubAccountVo.class,pageNo,pageSize,certId,remark,enabled);
        return response;
    }
    @RequestMapping(value = "/delete/{subId}")
    @ResponseBody
    public RestResponse delete(HttpServletRequest request,@PathVariable String subId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/delete/"+subId;
        RestResponse response =  RestRequest.buildSecurityRequest(token).get(uri,String.class);
        return response;
    }
    @RequestMapping(value = "/enabled/{subId}")
    @ResponseBody
    public RestResponse enabled(HttpServletRequest request,@PathVariable String subId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/enabled/"+subId;
        RestResponse response =  RestRequest.buildSecurityRequest(token).get(uri,String.class);
        return response;
    }
    @RequestMapping(value = "/disable/{subId}")
    @ResponseBody
    public RestResponse disable(HttpServletRequest request,@PathVariable String subId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL+"/rest/sub_account/disable/"+subId;
        RestResponse response =  RestRequest.buildSecurityRequest(token).get(uri,String.class);
        return response;
    }
}
