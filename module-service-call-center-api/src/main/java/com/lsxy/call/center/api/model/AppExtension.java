package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    private String name;//    name                 varchar(100) comment '名称',
    private String type;//    type                 varchar(30),
    private String user;//    user                 varchar(50) comment 'SIP注册用户名，全局唯一。格式是6到12位数字',
    private String password;//    password             varchar(50) comment 'SIP注册密码',
    private String secret;//    secret               varchar(100) comment '全局唯一的内部SIP登录地址获取密钥',
    private String registrar;//    registrar            varchar(100) comment 'SIP注册点地址。该分机需要向这个注册点进行注册。格式：<host>[:port]，默认端口5060',
    private String telenum;//    telenum              varchar(32) comment '如果是电话分机，该属性记录电话号码（保留，不用）',
    private String ext;//    ext                  varchar(32) comment '分机短号（保留，不用）',
    private String did;//    did                  varchar(32) comment '分机直通号（保留，不用）',

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

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "registrar")
    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    @Column(name = "telenum")
    public String getTelenum() {
        return telenum;
    }

    public void setTelenum(String telenum) {
        this.telenum = telenum;
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

}

