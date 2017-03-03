package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 应用分机
 * Created by zhangxb on 2016/10/21.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_app_extension")
public class AppExtension extends IdEntity {

    public final static String TYPE_SIP = "1";
    public final static String TYPE_THIRD_SIP = "2";
    public final static String TYPE_TELPHONE = "3";

    private String appId;//    app_id               varchar(32) comment '所属应用ID',
    private String tenantId; //tenant_id   租户id
    private String subaccountId;
    private String type;//    type                 varchar(30),
    private String user;//    user                 varchar(50) comment 'SIP注册用户名，全局唯一。格式是6到12位数字',
    private String password;//    password             varchar(50) comment 'SIP注册密码',
    private String secret;//    secret               varchar(100) comment '全局唯一的内部SIP登录地址获取密钥',
    private String telnum;//    telnum              varchar(32) comment '如果是电话分机，该属性记录电话号码（保留，不用）',
    private String ext;//    ext                  varchar(32) comment '分机短号（保留，不用）',
    private String did;//    did                  varchar(32) comment '分机直通号（保留，不用）',
    private String ipaddr;  //SIP 网关IP地址与端口，默认5060，仅用于 type==2的情况

    private Boolean enable; //分机是否可用（不存到数据库）

    public AppExtension() {
    }

    public AppExtension(String type, String user, String password) {
        this.type = type;
        this.user = user;
        this.password = password;
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

    @Column(name = "subaccount_id")
    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "user")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "secret")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Column(name = "telnum")
    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    @Column(name = "ext")
    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
    @Column(name = "did")
    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    @Column(name = "ipaddr")
    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    @Transient
    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}

