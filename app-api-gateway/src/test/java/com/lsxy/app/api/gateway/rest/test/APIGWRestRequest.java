package com.lsxy.app.api.gateway.rest.test;

import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestRequestConnectionConfig;

/**
 * Created by Tandy on 2016/7/1.
 * api 网关请求对象
 */
public class APIGWRestRequest extends RestRequest{



    protected APIGWRestRequest(RestRequestConnectionConfig config) {
        super(config);
    }

    /**
     * 构建一个单一实例的请求对象
     * 因为每次安全请求的token有可能不一样，建议每次构建请求对象重新生成，避免多线程不安全性问题
     *
     * @return
     */
    public static APIGWRestRequest buildSecurityRequest(String certid) {

        APIGWRestRequest securityRequest = new APIGWRestRequest(RestRequestConnectionConfig.defaultConfig());
        securityRequest.setSecurityToken(certid);
        return securityRequest;
    }



}
