package com.lsxy.app.api.gateway.security.auth;

/**
 * Created by Tandy on 2016/7/1.
 */
public class RestCredentials {
    private String requestData;
    private String signature;

    public RestCredentials(String requestData, String signature) {
        this.requestData = requestData;
        this.signature = signature;
    }

    public String getRequestData() {
        return requestData;
    }

    public String getSignature() {
        return signature;
    }
}
