package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 应用分机
 * Created by zhangxb on 2016/10/21.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_app_extension")
public class AppExtension extends IdEntity {
    private String appId;//    app_id               varchar(32) comment '所属应用ID',
    private String tenantId; //tenant_id   租户id
    private String name;//    name                 varchar(100) comment '名称',
    private Integer enabled;//    enabled              smallint comment '状态',
    private String type;//    type                 varchar(30),
    private String user;//    user                 varchar(50) comment 'SIP注册用户名，全局唯一。格式是6到12位数字',
    private String password;//    password             varchar(50) comment 'SIP注册密码',
    private String secret;//    secret               varchar(100) comment '全局唯一的内部SIP登录地址获取密钥',
    private String registrar;//    registrar            varchar(100) comment 'SIP注册点地址。该分机需要向这个注册点进行注册。格式：<host>[:port]，默认端口5060',
    private Integer registerExpires;//    register_expires     int comment '注册超过该时间后，需要重新中注册。该值应出现在SIP服务器返回的Register回复消息的Expires头域中。',
    private String telenum;//    telenum              varchar(32) comment '如果是电话分机，该属性记录电话号码（保留，不用）',
    private Date lastRegisterTime;//    last_register_time   datetime comment '最近注册成功的时间。注册成功后超过register_expires无新的成功注册，视为注册超时，当离线处理。',
    private Integer lastRegisterStatus;//    last_register_status int comment '最近注册状态 2xx: 成功。0表示没有任何注册',
    private Integer lastAction;//    last_action          int comment '最近的动作',
    private Date lastActionTime;//    last_action_time     datetime comment '最近的动作发生的时间',
    private String ext;//    ext                  varchar(32) comment '分机短号（保留，不用）',
    private String did;//    did                  varchar(32) comment '分机直通号（保留，不用）',
    private String agent;//    agent                varchar(32),
    @Column(name = "app_id ")
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "tenant_id ")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "name ")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "enabled ")
    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
    @Column(name = "type ")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "user ")
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    @Column(name = "password ")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Column(name = "secret ")
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
    @Column(name = "registrar ")
    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }
    @Column(name = "register_expires ")
    public Integer getRegisterExpires() {
        return registerExpires;
    }

    public void setRegisterExpires(Integer registerExpires) {
        this.registerExpires = registerExpires;
    }
    @Column(name = "telenum ")
    public String getTelenum() {
        return telenum;
    }

    public void setTelenum(String telenum) {
        this.telenum = telenum;
    }
    @Column(name = "last_register_time ")
    public Date getLastRegisterTime() {
        return lastRegisterTime;
    }

    public void setLastRegisterTime(Date lastRegisterTime) {
        this.lastRegisterTime = lastRegisterTime;
    }
    @Column(name = "last_register_status ")
    public Integer getLastRegisterStatus() {
        return lastRegisterStatus;
    }

    public void setLastRegisterStatus(Integer lastRegisterStatus) {
        this.lastRegisterStatus = lastRegisterStatus;
    }

    @Column(name = "last_action ")
    public Integer getLastAction() {
        return lastAction;
    }
    public void setLastAction(Integer lastAction) {
        this.lastAction = lastAction;
    }
    @Column(name = "last_action_time ")
    public Date getLastActionTime() {
        return lastActionTime;
    }

    public void setLastActionTime(Date lastActionTime) {
        this.lastActionTime = lastActionTime;
    }
    @Column(name = "ext ")
    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
    @Column(name = "did ")
    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }
    @Column(name = "agent ")
    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}

