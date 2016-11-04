package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * 坐席
 * Created by zhangxb on 2016/10/21.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center_agent")
public class CallCenterAgent extends IdEntity {
    public final static String STATE_NONE = "none";
    public final static String STATE_OFFLINE = "offline";
    public final static String STATE_ONLINE = "online";
    public final static String STATE_IDLE = "idle";
    public final static String STATE_TALKING = "talking";
    public final static String STATE_AWAY = "away";
    public final static String STATE_DEFAULT = STATE_ONLINE;
    private String tenantId;
    private String appId;
    private String name;
    private String state;

    private List<AppExtension> extentions;

    private List<AgentSkill> skills;


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

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Transient
    public List<AppExtension> getExtentions() {
        return extentions;
    }

    public void setExtentions(List<AppExtension> extentions) {
        this.extentions = extentions;
    }

    @Transient
    public List<AgentSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<AgentSkill> skills) {
        this.skills = skills;
    }
}

