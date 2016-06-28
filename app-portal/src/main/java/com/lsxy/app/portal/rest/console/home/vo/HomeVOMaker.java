package com.lsxy.app.portal.rest.console.home.vo;

import com.lsxy.app.portal.rest.comm.PortalConstants;
import com.lsxy.framework.tenant.model.Billing;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/6/27.
 */
public class HomeVOMaker {

    public static HomeVO getHomeVO(String token){
        HomeVO vo = new HomeVO();
        //此处调用restApi
        String url = PortalConstants.REST_PREFIX_URL + "/rest/billing/get";
        Map<String,Object> formParams = new HashMap<>();
        formParams.put("username","user001");
        formParams.put("password","password");
        RestResponse<Billing> response = RestRequest.buildSecurityRequest(token).post(url,formParams,Billing.class);

        //获取余额
        Billing balance = response.getData();
        return null;
    }

}
