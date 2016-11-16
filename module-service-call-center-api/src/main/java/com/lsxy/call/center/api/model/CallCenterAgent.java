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
    private String channel;
    private String name;//坐席id
    private String num;//坐席工号

    @Transient
    private String state;
    @Transient
    private List<AgentSkill> skills;
    @Transient
    private String extension;


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

    @Column(name = "channel")
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "num")
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Transient
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Transient
    public List<AgentSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<AgentSkill> skills) {
        this.skills = skills;
    }

    @Transient
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}

