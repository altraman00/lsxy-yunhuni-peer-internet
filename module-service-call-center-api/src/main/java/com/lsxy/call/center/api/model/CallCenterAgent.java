package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    private String channelId;
    private String agentNo;//坐席id
    private String agentNum;//坐席工号


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

    @Column(name = "channel_id")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Column(name = "agent_no")
    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    @Column(name = "agent_num")
    public String getAgentNum() {
        return agentNum;
    }

    public void setAgentNum(String agentNum) {
        this.agentNum = agentNum;
    }
}

