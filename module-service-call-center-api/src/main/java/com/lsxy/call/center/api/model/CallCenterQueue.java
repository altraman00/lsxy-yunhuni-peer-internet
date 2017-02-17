package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liuws on 2016/11/14.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center_queue")
public class CallCenterQueue extends IdEntity {
    public static final String RESULT_FAIL = "fail";
    public static final String RESULT_SELETEED = "selected";
    public static final String RESULT_DIAL_SUCC = "dail_succ";
    public static final String RESULT_DIAL_FAIL = "dial_fail";

    private String tenantId;
    private String appId;
    private String condition;
    private String relevanceId;
    private String type;
    private Date startTime;
    private Date inviteTime;
    private Date dialTime;
    private Date endTime;
    private Long toManualTime;
    private String result;
    private String num;
    private String agent;
    private String originCallId;
    private String agentCallId;
    private String conversation;
    private Integer fetchTimeOut;
    private String enqueue;//存放排队的json

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

    @Column(name = "queue_condition")
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "result")
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Column(name = "queue_num")
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Column(name = "queue_agent")
    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }


    @Column(name = "invite_time")
    public Date getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(Date inviteTime) {
        this.inviteTime = inviteTime;
    }

    @Column(name = "dial_time")
    public Date getDialTime() {
        return dialTime;
    }

    public void setDialTime(Date dialTime) {
        this.dialTime = dialTime;
    }

    @Column(name = "to_manual_time")
    public Long getToManualTime() {
        return toManualTime;
    }

    public void setToManualTime(Long toManualTime) {
        this.toManualTime = toManualTime;
    }

    @Column(name = "origin_call_id")
    public String getOriginCallId() {
        return originCallId;
    }

    public void setOriginCallId(String originCallId) {
        this.originCallId = originCallId;
    }

    @Column(name = "agent_call_id")
    public String getAgentCallId() {
        return agentCallId;
    }

    public void setAgentCallId(String agentCallId) {
        this.agentCallId = agentCallId;
    }

    @Column(name = "conversation_id")
    public String getConversation() {
        return conversation;
    }

    public void setConversation(String conversation) {
        this.conversation = conversation;
    }

    @Column(name = "fetch_timeout")
    public Integer getFetchTimeOut() {
        return fetchTimeOut;
    }

    public void setFetchTimeOut(Integer fetchTimeOut) {
        this.fetchTimeOut = fetchTimeOut;
    }

    @Column(name = "enqueue")
    public String getEnqueue() {
        return enqueue;
    }

    public void setEnqueue(String enqueue) {
        this.enqueue = enqueue;
    }
}
