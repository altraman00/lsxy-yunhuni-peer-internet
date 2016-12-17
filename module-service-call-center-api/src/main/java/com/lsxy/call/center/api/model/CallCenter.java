package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangxb on 2016/10/22.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center")
public class CallCenter extends IdEntity {
    public static int CALL_UP = 2;//呼出
    public static int CALL_IN = 1;//呼入
    public static int CALL_DIAL = 3;//拨号

    public static int TO_MANUAL_RESULT_SUCESS = 1;//接听
    public static int TO_MANUAL_RESULT_AGENT_FAIL = 2; //呼叫坐席失败
    public static int TO_MANUAL_RESULT_FAIL = 3; //主动放弃
    public static int TO_MANUAL_RESULT_TIME_OUT = 4;//超时
    @Column( name = "tenant_id")
    private String tenantId;
    @Column( name = "app_id")
    private String appId;
    @Column( name = "start_time")
    private Date startTime;//发起时间
    @Column( name = "answer_time")
    private Date answerTime;//接听时间
    @Column( name = "end_time")
    private Date endTime;//结束时间
    @Column( name = "call_time_long")
    private String callTimeLong;//通话时间
    @Column( name = "from_num")
    private String fromNum;//主叫号
    @Column( name = "to_num")
    private String toNum; //被叫号
    @Column( name = "type")
    private String type;//1:呼入2:呼出
    @Column( name = "cost")
    private BigDecimal cost;//费用
    @Column( name = "to_manual_time")
    private String toManualTime;//转人工时间
    @Column( name = "to_manual_result")
    private String toManualResult;//转接结果
    @Column( name = "agent")
    private String agent;//座席
    @Column( name = "over_reason")
    private String overReason;//结束原因

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getCallTimeLong() {
        return callTimeLong;
    }

    public void setCallTimeLong(String callTimeLong) {
        this.callTimeLong = callTimeLong;
    }

    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }

    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getToManualTime() {
        return toManualTime;
    }

    public void setToManualTime(String toManualTime) {
        this.toManualTime = toManualTime;
    }

    public String getToManualResult() {
        return toManualResult;
    }

    public void setToManualResult(String toManualResult) {
        this.toManualResult = toManualResult;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getOverReason() {
        return overReason;
    }

    public void setOverReason(String overReason) {
        this.overReason = overReason;
    }
}
