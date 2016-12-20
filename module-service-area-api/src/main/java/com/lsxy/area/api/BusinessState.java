package com.lsxy.area.api;

import com.lsxy.framework.core.utils.JSONUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/8/25.
 */
public class BusinessState implements Serializable{
    public static final String TYPE_IVR_INCOMING = "ivr_incoming";
    public static final String TYPE_IVR_CALL = "ivr_call";
    public static final String TYPE_IVR_DIAL = "ivr_dial";
    public static final String TYPE_NOTIFY_CALL = "notify_call";
    public static final String TYPE_VERIFY_CALL = "verify_call";
    public static final String TYPE_DUO_CALL = "duo_call";
    public static final String TYPE_SYS_CONF = "sys_conf";
    public static final String TYPE_CC_CONVERSATION = "conversation";

    public static final String TYPE_CC_INCOMING = "cc_incoming";
    public static final String TYPE_CC_AGENT_CALL = "agent_call";
    public static final String TYPE_CC_OUT_CALL = "out_call";

    public static final String SESSIONID = "sessionid";

    private String tenantId;
    private String appId;
    private String id;
    private String type;
    private String userdata;
    private String resId;
    private String callBackUrl;
    private String areaId;
    private String lineGatewayId;
    private Boolean closed;
    private Map<String,String> businessData;

    private BusinessState() {
    }

    private BusinessState(String tenantId, String appId, String id, String type, String userdata, String resId, String callBackUrl, String areaId, String lineGatewayId,Boolean closed, Map<String, String> businessData) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.id = id;
        this.type = type;
        this.userdata = userdata;
        this.resId = resId;
        this.callBackUrl = callBackUrl;
        this.areaId = areaId;
        this.lineGatewayId = lineGatewayId;
        this.closed = closed;
        if(businessData == null){
            businessData = new HashMap<>();
        }
        this.businessData = businessData;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUserdata() {
        return userdata;
    }

    public String getResId() {
        return resId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getAppId() {
        return appId;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getLineGatewayId() {
        return lineGatewayId;
    }

    public Boolean getClosed() {
        return closed;
    }

    public Map<String, String> getBusinessData() {
        return businessData;
    }

    @Override
    public String toString(){
        return JSONUtil.objectToJson(this);
    }

    public static class Builder{

        private String tenantId;
        private String appId;
        private String id;
        private String type;
        private String userdata;
        private String resId;
        private String callBackUrl;
        private String areaId;
        private String lineGatewayId;
        private Boolean closed;

        private Map<String,String> businessData;

        public Builder(){}

        public Builder setId(String id){
            this.id=id;
            return this;
        }

        public Builder setTenantId(String tenantId) {
            this.tenantId = tenantId;
            return this;
        }

        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setUserdata(String userdata) {
            this.userdata = userdata;
            return this;
        }

        public Builder setResId(String resId) {
            this.resId = resId;
            return this;
        }

        public Builder setCallBackUrl(String callBackUrl) {
            this.callBackUrl = callBackUrl;
            return this;
        }

        public Builder setAreaId(String areaId) {
            this.areaId = areaId;
            return this;
        }

        public Builder setLineGatewayId(String lineGatewayId) {
            this.lineGatewayId = lineGatewayId;
            return this;
        }

        public Builder setClosed(Boolean closed) {
            this.closed = closed;
            return this;
        }

        public Builder setBusinessData(Map<String, String> businessData) {
            this.businessData = businessData;
            return this;
        }

        public BusinessState build(){
            return new BusinessState(tenantId,appId,id,type,userdata,resId,callBackUrl,areaId,lineGatewayId,closed,businessData);
        }
    }
}
