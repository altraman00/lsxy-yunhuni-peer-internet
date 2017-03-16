package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.app.portal.console.app.vo.MsgTemplateVo;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lsxy.framework.web.rest.RestRequest.buildSecurityRequest;

/**
 * Created by zhangxb on 2017/3/7.
 */
@Controller
@RequestMapping("/console/msg_template")
public class MsgTemplateController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(MsgTemplateController.class);
    @RequestMapping("/plist")
    @ResponseBody
    public RestResponse getPage(HttpServletRequest request,@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "20") int pageSize, @RequestParam String appId, String name, String subId ){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/plist?pageNo={1}&pageSize={2}&appId={3}&name={4}&subId={5}";
        RestResponse<Page<MsgTemplate>> response =  buildSecurityRequest(token).getPage(url,MsgTemplate.class,pageNo,pageSize,appId,name,subId);
        if(response.isSuccess()){
            Page<MsgTemplate> page = response.getData();
            if(page != null){
                List<MsgTemplateVo> returnResult = new ArrayList<>();
                List<MsgTemplate> result = page.getResult();
                Map<String,ApiCertificateSubAccount> map = getMapSubAccountList(request,appId);
                if(result != null){
                    result.stream().forEach(msgTemplate -> returnResult.add(MsgTemplateVo.changeMsgTemplateToVo(msgTemplate,map.get(msgTemplate.getSubaccountId()))));
                    page.setResult(returnResult);
                }
            }
        }
        return response;
    }
    @RequestMapping(value = "/new")
    @ResponseBody
    public RestResponse create(HttpServletRequest request, String appId, String type, String name, String content, String remark){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/new";
        Map<String,Object> map = new HashMap<String,Object>(){
            {
                put("appId",appId);
                put("type",type);
                put("name",name);
                put("content",content);
                put("remark",remark);
            }
        };
        RestResponse response =  buildSecurityRequest(token).post(url,map,String.class);
        return response;
    }
    @RequestMapping(value = "/edit/{id}")
    @ResponseBody
    public RestResponse edit(HttpServletRequest request, @PathVariable String id, String name, String content, String remark){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/edit/"+id;
        Map<String,Object> map = new HashMap<String,Object>(){
            {
                put("name",name);
                put("content",content);
                put("remark",remark);
            }
        };
        RestResponse response =  buildSecurityRequest(token).post(url,map,String.class);
        return response;
    }
    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public RestResponse delete(HttpServletRequest request, @PathVariable String id){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/delete/"+id;
        RestResponse response =  buildSecurityRequest(token).get(url,String.class);
        return response;
    }
    @RequestMapping(value = "/get/{id}")
    @ResponseBody
    public RestResponse findById(HttpServletRequest request, @PathVariable String id){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/msg_template/find/"+id;
        RestResponse response =  buildSecurityRequest(token).get(url,MsgTemplate.class);
        return response;
    }
}
