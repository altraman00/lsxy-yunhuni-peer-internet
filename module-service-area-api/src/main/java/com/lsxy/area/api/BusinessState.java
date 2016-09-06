package com.lsxy.area.api;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by liups on 2016/8/25.
 */
public class BusinessState implements Serializable{
    private String tenantId;
    private String appId;
    private String id;
    private String type;
    private String userdata;
    private String resId;
    private String callBackUrl;
    private String areaId;
    private String lineGatewayId;
    private Map<String,Object> businessData;

    public BusinessState() {
    }

    public BusinessState(String tenantId, String appId, String id, String type,String userdata, String callBackUrl,String areaId, String lineGatewayId, Map<String,Object> map) {
        this(tenantId,appId,id,type,userdata,null,callBackUrl,areaId,lineGatewayId,map);
    }

    public BusinessState(String tenantId, String appId, String id, String type, String userdata, Map<String,Object> businessData) {
        this(tenantId,appId,id,type,userdata,null,null,businessData);
    }

    public BusinessState(String tenantId, String appId, String id, String type, String userdata, String resId, String callBackUrl,Map<String,Object> businessData) {
        this(tenantId,appId,id,type,userdata,resId,callBackUrl,null,null,businessData);
    }

    public BusinessState(String tenantId, String appId, String id, String type, String userdata, String resId, String callBackUrl, String areaId, String lineGatewayId, Map<String, Object> businessData) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.id = id;
        this.type = type;
        this.userdata = userdata;
        this.resId = resId;
        this.callBackUrl = callBackUrl;
        this.areaId = areaId;
        this.lineGatewayId = lineGatewayId;
        this.businessData = businessData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserdata() {
        return userdata;
    }

    public void setUserdata(String userdata) {
        this.userdata = userdata;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public Map<String, Object> getBusinessData() {
        return businessData;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setBusinessData(Map<String, Object> businessData) {
        this.businessData = businessData;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLineGatewayId() {
        return lineGatewayId;
    }

    public void setLineGatewayId(String lineGatewayId) {
        this.lineGatewayId = lineGatewayId;
    }
}
