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

    public static final String STATE_ONLINE = "online";
    public static final String STATE_IDLE = "idle";
    public static final String STATE_FETCHING = "fetching";
    public static final String STATE_TALKING = "talking";
    public static final String STATE_BUSY = "busy";
    public static final String STATE_AWAY = "away";

    private String tenantId;
    private String appId;
    private String subaccountId;
    private String name;//坐席id
    private String num;//坐席工号


    private String state;   //座席状态，不存入座席表
    private List<AgentSkill> skills; //座席技能，不存入座席表
    private String extension;   //座席分机，不存入座席表


    public static String getChineseState(String state){
        state = state.replace(STATE_ONLINE,"在线");
        state = state.replace(STATE_IDLE,"空闲");
        state = state.replace(STATE_FETCHING,"正在呼叫");
        state = state.replace(STATE_TALKING,"通话");
        state = state.replace(STATE_BUSY,"忙碌");
        state = state.replace(STATE_AWAY,"离席");
        return state;
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

