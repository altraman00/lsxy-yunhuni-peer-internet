package com.oneyun.sapi.config;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/9/20.
 */
public class SapiSdkConfig  implements Serializable{

    private static final long serialVersionUID = 1L;

    //请求地址
    private String url ;
    //鉴权账号 用户中心获取
    private String certId ;
    //SecretKey 用户中心获取
    private String secretKey ;
    /**
     * 组装符合要求的url
     * @param type 业务类型
     * @return
     */
    public String getUrl(String type){
        return url+type;
    }
    /**
     * 组装符合要求的url
     * @param type 业务类型
     * @param config 会议类型
     * @return
     */
    public  String getUrl(String type,String config){
        int s1 = type.indexOf("{");
        int s2 = type.lastIndexOf("}");
        return url+type.substring(0,s1)+config+type.substring(s2+1,type.length());
    }
    public SapiSdkConfig(String url, String certId, String secretKey) {
        this.url = url;
        this.certId = certId;
        this.secretKey = secretKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
