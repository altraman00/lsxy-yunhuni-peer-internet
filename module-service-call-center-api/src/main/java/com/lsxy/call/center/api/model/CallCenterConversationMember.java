package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 呼叫中心交互成员
 * Created by zhangxb on 2016/11/11.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center_conversation_member")
public class CallCenterConversationMember extends IdEntity {
    public static final String INITIATOR_TRUE = "1";
    public static final String INITIATOR_FALSE = "0";
    public static final String AGENT_TRUE = "1";
    public static final String AGENT_FALSE = "0";

    public static final Integer MODE_I_O=1;//收放音
    public static final Integer MODE_I = 2;//收音
    public static final Integer MODE_O = 3;//放音
    public static final Integer MODE_N = 4;//无
    public static final Integer MODE_DEFAULT = MODE_I_O;

    private String relevanceId;//所属呼叫中心交谈
    private String sessionId;//加入的session
    private String callId;
    private Date startTime;//发起时间
    private Date endTime;//结束时间
    private String joinNum;//加入的号码
    private String isInitiator;//是否发起方
    private String isAgent;//是否坐席
    private String agentName;//坐席name
    private String extensionId;//坐席的分机id
    private Integer mode;//说听模式

    @Column(name = "call_id")
    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    @Column(name = "relevance_id")
    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
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
    @Column(name = "join_num")
    public String getJoinNum() {
        return joinNum;
    }

    public void setJoinNum(String joinNum) {
        this.joinNum = joinNum;
    }
    @Column(name = "is_initiator")
    public String getIsInitiator() {
        return isInitiator;
    }

    public void setIsInitiator(String isInitiator) {
        this.isInitiator = isInitiator;
    }
    @Column(name = "session_id")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name = "is_agent")
    public String getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(String isAgent) {
        this.isAgent = isAgent;
    }

    @Column(name = "agent_name")
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Column(name = "extension_id")
    public String getExtensionId() {
        return extensionId;
    }

    public void setExtensionId(String extensionId) {
        this.extensionId = extensionId;
    }

    @Column(name = "mode")
    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
