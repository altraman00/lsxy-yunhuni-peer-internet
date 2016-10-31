package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lsxy.framework.web.rest.RestRequest.buildSecurityRequest;
import static com.lsxy.framework.web.rest.RestResponse.failed;

/**
 * 应用上线动作
 * Created by liups on 2016/7/15.
 */
@Controller
@RequestMapping("/console/app_action")
public class AppOnlineActionController extends AbstractPortalController {

    /**
     * 获取应用的上线步骤
     * @param request
     * @param appId 应用ID
     * @return
     */
    @RequestMapping(value = "/{appId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getOnlineAction(HttpServletRequest request,@PathVariable String appId){
        RestResponse result;
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/app_online/{1}";
        RestResponse<AppOnlineAction> response =  buildSecurityRequest(token).get(url,AppOnlineAction.class,appId);
        if(response.isSuccess()){
            Integer action = null;
            if(response.getData() != null){
                action = response.getData().getAction();
            }
            result = RestResponse.success(action);
        }else{
            result = failed(response.getErrorCode(),response.getErrorMsg());
        }
        return result;
    }

    /**
     * 获取可供选择的号码
     * @param request
     * @param appId
     * @return
     */
    @RequestMapping(value = "/select_num/{appId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getSelectNum(HttpServletRequest request, @PathVariable String appId){
        String token = getSecurityToken(request);
        RestResponse result;
        //是否实名认证
        boolean isRealAuth = findAuthStatus(token);
        if(isRealAuth){
            String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/select_num/{1}";
            RestResponse<Map> response = buildSecurityRequest(token).get(url,Map.class,appId);
            Map data = response.getData();
            if(response.isSuccess()){
                result = RestResponse.success(data);
            }else{
                result = failed(response.getErrorCode(),response.getErrorMsg());
            }
        }else{
            result =RestResponse.failed("0000","用户未实名认证");
        }
        return result;
    }

    /**
     * 选定号码后上线
     * @param request
     * @param appId
     * @return
     */
    @RequestMapping(value = "/online",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse online(HttpServletRequest request,String appId,String nums){
        String token = getSecurityToken(request);
        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/online?appId={1}&nums={2}";
        RestResponse<AppOnlineAction> response = buildSecurityRequest(token).get(url,AppOnlineAction.class,appId,nums);
        return response;
    }


    /**
     * 应用下线
     * @param request
     * @param appId
     * @return
     */
    @RequestMapping(value = "/offline",method = RequestMethod.POST)
    @ResponseBody
    public RestResponse offline(HttpServletRequest request,String appId){

        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/offline?appId={1}";
        RestResponse<App> response = buildSecurityRequest(getSecurityToken(request)).get(url,App.class,appId);

        return response;
    }

    /**
     * 获取后台状态的rest请求方法
     * @return
     */
    private boolean findAuthStatus(String token){
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/account/auth/find_auth_status";
        RestResponse response = RestRequest.buildSecurityRequest(token).get(uri, HashMap.class);
        boolean flag = false;
        if(response.isSuccess() && response.getData() != null){
            Map result = (Map) response.getData();
            int authStatus  = Integer.valueOf((result.get("status")+""));
            if (Tenant.AUTH_ONESELF_SUCCESS == authStatus || Tenant.AUTH_COMPANY_SUCCESS == authStatus) {
                flag = true;
            }
        }
        return flag;
    }
}
