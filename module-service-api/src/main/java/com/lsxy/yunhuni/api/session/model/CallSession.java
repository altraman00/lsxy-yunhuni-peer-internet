package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by liups on 2016/6/29.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_session")

public class CallSession extends IdEntity {
    public static final int STATUS_PREPARING = 0;
    public static final int STATUS_CALLING = 1;
    public static final int STATUS_OVER = 2;
    public static final int STATUS_EXCEPTION = 3;
    //TODO 换成与ProductCode枚举关联
    public static final String TYPE_VOICE_NOTIFY = ProductCode.notify_call.name();
    public static final String TYPE_VOICE_CALLBACK = ProductCode.duo_call.name();
    public static final String TYPE_VOICE_MEETING = ProductCode.sys_conf.name();
    public static final String TYPE_VOICE_IVR = ProductCode.ivr_call.name();
    public static final String TYPE_VOICE_VOICECODE = ProductCode.captcha_call.name();
    public static final String TYPE_CALL_CENTER = ProductCode.call_center.name();
    public static final String[] CALL_CENTER_PRODUCT_CODE = {ProductCode.call_center.name(),ProductCode.call_center_sip.name()};//呼叫中心产品计费项目
    private Integer status;         //状态
    private String appId;            //所属APP
    private String tenantId;      //所属tenant
    private String relevanceId;//关联标识
    private String type; //查看产品表code字段或枚举类ProductCode
    private String resId; //资源ID
    private String fromNum; //发起方
    private String toNum; //接收方

    public CallSession() {
    }

    public CallSession(Integer status, String appId, String tenantId, String relevanceId, String type, String fromNum, String toNum) {
        this.status = status;
        this.appId = appId;
        this.tenantId = tenantId;
        this.relevanceId = relevanceId;
        this.type = type;
        this.fromNum = fromNum;
        this.toNum = toNum;
    }

    public CallSession(String id,Integer status, String appId, String tenantId, String relevanceId, String type, String fromNum, String toNum) {
        this.id = id;
        this.status = status;
        this.appId = appId;
        this.tenantId = tenantId;
        this.relevanceId = relevanceId;
        this.type = type;
        this.fromNum = fromNum;
        this.toNum = toNum;
    }

    @Column(name = "relevance_id")
    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "res_id")
    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "from_num")
    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }

    @Column(name = "to_num")
    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }
}
