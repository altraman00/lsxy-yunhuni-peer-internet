package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.billing.model.Billing;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    @RequestMapping(value = "/select_ivr/{appId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getSelectIvr(HttpServletRequest request,@PathVariable String appId){
        String token = getSecurityToken(request);
        RestResponse result;
        //是否实名认证
        boolean isRealAuth = findAuthStatus(token);
        if(isRealAuth){
            String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/select_ivr/{1}";
            RestResponse<Map> response = buildSecurityRequest(token).get(url,Map.class,appId);
            Map<String,Object> data = response.getData();
            if(response.isSuccess() && data != null && (data.get("selectIvr") != null || data.get("ownIvr") != null)){
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
     * 生成或获取支付订单
     * @param request
     * @param appId
     * @param ivr
     * @return
     */
    @RequestMapping(value = "/get_pay",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getPay(HttpServletRequest request,String appId,String ivr){
        String token = getSecurityToken(request);
        Map<String,Object> data = new HashMap<>();
        RestResponse result;
        //获取当前动作
        RestResponse<AppOnlineAction> response = getPayRest(token, appId,ivr);
        AppOnlineAction action = response.getData();
        //获取账户余额
        RestResponse<Billing> billingResponse = getBilling(token);
        Billing billing = billingResponse.getData();
        if(response.isSuccess() && action != null && billing != null){
            data.put("action",action);
            data.put("balance",billing.getBalance());
            result = RestResponse.success(data);
        }else{
//            data.put("flag",false);
//            data.put("msg","数据异常");
            result = RestResponse.failed("0000","数据异常");
        }
        return result;
    }


    /**
     * 获取支付页面rest调用
     * @param token
     * @param appId
     * @return
     */
    private RestResponse<AppOnlineAction> getPayRest(String token, String appId,String ivr) {
        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/get_pay?appId={1}&ivr={2}";
        return buildSecurityRequest(token).get(url,AppOnlineAction.class,appId,ivr);
    }


    /**
     * 支付上线
     * @param request
     * @param appId
     * @return
     */
    @RequestMapping(value = "/pay",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse pay(HttpServletRequest request,String appId){
        String token = getSecurityToken(request);
        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/pay?appId={1}";
        RestResponse<AppOnlineAction> response = buildSecurityRequest(token).get(url,AppOnlineAction.class,appId);

        return response;
    }


    /**
     * 直接上线（没有IVR功能）
     * @param request
     * @param appId
     * @return
     */
    @RequestMapping(value = "/direct_online",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse directOnline(HttpServletRequest request,String appId){
        String token = getSecurityToken(request);
        RestResponse result;
        //是否实名认证
        boolean isRealAuth = findAuthStatus(token);
        if(isRealAuth){
            result = directOnlineRest(token,appId);

        }else{
            result = RestResponse.failed("0000","用户未实名认证");

        }
        return result;
    }


    /**
     * 直接上线rest调用
     * @param token
     * @param appId
     * @return
     */
    private RestResponse<AppOnlineAction> directOnlineRest(String token, String appId) {
        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/direct_online?appId={1}";
        return buildSecurityRequest(token).get(url,AppOnlineAction.class,appId);
    }

    /**
     * 重新选择IVR号吗
     * @param request
     * @param appId
     * @return
     */
    @RequestMapping(value = "/reset_ivr",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse resetIvr(HttpServletRequest request,String appId){

        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/reset_ivr?appId={1}";
        RestResponse<AppOnlineAction> response = buildSecurityRequest(getSecurityToken(request)).get(url,AppOnlineAction.class,appId);

        return response;
    }

    /**
     * 重设IVR号码rest调用
     * @param token
     * @param appId
     * @return
     */
    private RestResponse<AppOnlineAction> resetIvrRest(String token, String appId) {
        String url =  PortalConstants.REST_PREFIX_URL + "/rest/app_online/reset_ivr?appId={1}";
        return buildSecurityRequest(token).get(url,AppOnlineAction.class,appId);
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
     * 获取账务信息
     * @param token
     * @return
     */
    private RestResponse<Billing> getBilling(String token) {
        //此处调用账务restApi
        String billingUrl = PortalConstants.REST_PREFIX_URL + "/rest/billing/get";
        return buildSecurityRequest(token).get(billingUrl,Billing.class);
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
            if (Tenant.AUTH_ONESELF_SUCESS == authStatus || Tenant.AUTH_COMPANY_SUCESS == authStatus) {
                flag = true;
            }
        }
        return flag;
    }

}
