package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liups on 2016/11/15.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center_agent_action_log")
public class AgentActionLog extends IdEntity {
    public static int ACTION_LOGIN = 1;
    public static int ACTION_LOGOUT = 0;

    private String tenantId;
    private String appId;
    private String subaccountId;
    private String name;
    private Integer action; //1登录，0注销

    public AgentActionLog() {
    }

    public AgentActionLog(String tenantId, String appId, String subaccountId, String name, Integer action) {
        this.tenantId = tenantId;
        this.appId = appId;
        this.subaccountId = subaccountId;
        this.name = name;
        this.action = action;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "subaccount_id")
    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "action")
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }
}
