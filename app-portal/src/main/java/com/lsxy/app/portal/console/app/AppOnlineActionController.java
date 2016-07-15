package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用上线动作
 * Created by liups on 2016/7/15.
 */
@Controller
@RequestMapping("/console/app_online")
public class AppOnlineActionController extends AbstractPortalController {
    /**
     * 获取应用的上线步骤
     * @param request
     * @param appId 应用ID
     * @return
     */
    @RequestMapping(value = "get_action",method = RequestMethod.GET)
    public Map<String,Object> getOnlineAction(HttpServletRequest request, String appId){
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);
        RestResponse<AppOnlineAction> response = getOnlineActionRest(token,appId);
        if(response.isSuccess()){
            model.put("flag",true);
            if(response.getData() != null){
                model.put("onlineStep",response.getData());
            }
        }else{
            model.put("flag",false);
            model.put("err",response.getErrorMsg());
        }
        return model;
    }

    private RestResponse<AppOnlineAction> getOnlineActionRest(String token, String appId) {
        String url = PortalConstants.REST_PREFIX_URL + "/rest/app_online/get_action?appId={1}";
        return RestRequest.buildSecurityRequest(token).get(url,AppOnlineAction.class,appId);
    }

    @RequestMapping(value = "new_action",method = RequestMethod.GET)
    public Map<String,Object> newOnlineAction(HttpServletRequest request,String appId,String step){
        Map<String,Object> model = new HashMap<>();
        return model;
    }

    private RestResponse<Boolean> newOnlineActionRest(String token,String appId,String step){
        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/new_action?appId={1}&step={2}";
        return RestRequest.buildSecurityRequest(token).get(url,Boolean.class,appId,step);
    }
}
