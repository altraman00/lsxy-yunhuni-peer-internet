package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.console.app.vo.SubAccountVo;
import com.lsxy.app.portal.console.telenum.AppNumVO;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @RequestMapping(value = "/num/bind/app/{appId}/{subId}")
    @ResponseBody
    public RestResponse bindNumToApp(HttpServletRequest request,@PathVariable String appId,@PathVariable String subId,String nums){
        if(StringUtils.isBlank(nums)){
            return RestResponse.failed("0000","没有选定绑定的号码");
        }
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/sub_account/bind/num/"+appId+"/"+subId+"?nums={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, Object.class,nums);
    }
    @RequestMapping(value = "/unbind/all/{appId}/{subId}")
    @ResponseBody
    public RestResponse unbindAll(HttpServletRequest request,@PathVariable String appId,@PathVariable String subId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/sub_account/unbind/all/"+appId+"/"+subId;
        return RestRequest.buildSecurityRequest(token).get(uri, Object.class);
    }
    @RequestMapping(value = "/unbind/one/{appId}/{subId}/{numId}")
    @ResponseBody
    public RestResponse unbindOne(HttpServletRequest request,@PathVariable String appId,@PathVariable String subId, @PathVariable String numId){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/sub_account/unbind/one/"+appId+"/"+subId+"/"+numId;
        return RestRequest.buildSecurityRequest(token).get(uri, Object.class);
    }
    @RequestMapping(value = "/num/list")
    @ResponseBody
    public RestResponse numList(HttpServletRequest request,@RequestParam String appId, @RequestParam String subId, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/sub_account/num/list?appId={1}&subId={2}&pageNo={3}&pageSize={4}";
        RestResponse<Page<ResourcesRent>> response =  RestRequest.buildSecurityRequest(token).getPage(uri, ResourcesRent.class,appId,subId,pageNo,pageSize);
        if(!response.isSuccess()){
            return response;
        }
        Page page =  response.getData();
        List<ResourcesRent> result = page.getResult();
        if(result != null && result.size() > 0){
            List<AppNumVO> appNumVOs = new ArrayList<>();
            for(ResourcesRent rent : result){
                AppNumVO vo = new AppNumVO();
                vo.setRentId(rent.getResourceTelenum().getId());
                vo.setNum(rent.getResourceTelenum().getTelNumber());
                vo.setStatus((rent.getRentExpire() == null || rent.getRentExpire().compareTo(new Date()) == 1 )? "1" : "0");
                vo.setIsCalled(StringUtils.isBlank(rent.getResourceTelenum().getIsCalled())? ResourceTelenum.ISCALLED_FALSE:rent.getResourceTelenum().getIsCalled());
                if(ResourceTelenum.ISDIALING_TRUE.equals(rent.getResourceTelenum().getIsDialing()) || ResourceTelenum.ISTHROUGH_TRUE.equals(rent.getResourceTelenum().getIsThrough())){
                    vo.setIsDialing("1");
                }else{
                    vo.setIsDialing("0");
                }
                vo.setAreaCode(rent.getResourceTelenum().getAreaCode());
                vo.setExpireTime(rent.getRentExpire() == null ? "" : DateUtils.formatDate(rent.getRentExpire(),"yyyy-MM-dd "));
                appNumVOs.add(vo);
            }
            page.setResult(appNumVOs);
        }
        return RestResponse.success(page);
    }
    @RequestMapping(value = "/by/{appId}/list")
    @ResponseBody
    public RestResponse findByAppId(HttpServletRequest request,@PathVariable String appId){
        return getSubAccountList(request,appId);
    }
}
