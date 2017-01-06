package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 通话记录
 * Created by zhangxb on 2016/7/19.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_cdr")
public class VoiceCdr extends IdEntity {
    public static final int COST_TYPE_DEDUCT = 1;
    public static final int COST_TYPE_COST = 2;
    public static final int COST_TYPE_COST_DEDUCT = 3;
    private String sessionId;//会话ID
    private String areaId;//所属区域
    private String tenantId;//所属租户
    private String  appId;//所属应用
    private String lineId;//所属线路
    private BigDecimal lineCost;//线路成本
    private String type;//'查看产品表code字段或枚举类ProductCode
    private String relevanceId;//根据会话类型关联对应类型的表的记录
    private Integer recording; //是否有录音文件:0无，1有，null无法判断
    private String fromNum;//主叫
    private String toNum;//被叫
    private Date callStartDt;//呼叫开始时间
    private Date callAckDt;//呼叫应答时间
    private Date callEndDt;//呼叫结束时间
    private Long callTimeLong;//呼叫时长
    private Long costTimeLong;//扣费时长
    private BigDecimal cost;//消费金额
    private Long deduct;//扣量
    private String unit;//计量单位
    private Integer costType;//1.扣量2.扣费3.扣量加扣费
    private Integer joinType;//0创建1邀请加入，2呼入加入
    private Integer ivrType;//ivr类型1:呼入2呼出

    /*
        cdr原始数据，详情请参考文档
     */
    private String cdrOriginal;

    @Column(name = "ivr_type")
    public Integer getIvrType() {
        return ivrType;
    }

    public void setIvrType(Integer ivrType) {
        this.ivrType = ivrType;
    }

    @Column( name = "join_type")
    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    @Column( name = "session_id")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    @Column( name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
    @Column( name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column( name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Column( name = "line_id")
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
    @Column( name = "line_cost")
    public BigDecimal getLineCost() {
        return lineCost;
    }

    public void setLineCost(BigDecimal lineCost) {
        this.lineCost = lineCost;
    }

    @Column( name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column( name = "relevance_id")
    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }

    @Column( name = "recording")
    public Integer getRecording() {
        return recording;
    }

    public void setRecording(Integer recording) {
        this.recording = recording;
    }

    @Column( name = "from_num")
    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }
    @Column( name = "to_num")
    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }
    @Column( name = "call_start_dt")
    public Date getCallStartDt() {
        return callStartDt;
    }

    public void setCallStartDt(Date callStartDt) {
        this.callStartDt = callStartDt;
    }
    @Column( name = "call_ack_dt")
    public Date getCallAckDt() {
        return callAckDt;
    }

    public void setCallAckDt(Date callAckDt) {
        this.callAckDt = callAckDt;
    }
    @Column( name = "call_end_dt")
    public Date getCallEndDt() {
        return callEndDt;
    }

    public void setCallEndDt(Date callEndDt) {
        this.callEndDt = callEndDt;
    }

    @Column( name = "call_time_long")
    public Long getCallTimeLong() {
        return callTimeLong;
    }

    public void setCallTimeLong(Long callTimeLong) {
        this.callTimeLong = callTimeLong;
    }

    public Long getCostTimeLong() {
        return costTimeLong;
    }

    @Column( name = "cost_time_long")
    public void setCostTimeLong(Long costTimeLong) {
        this.costTimeLong = costTimeLong;
    }

    @Column( name = "cost")
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    @Column( name = "deduct")
    public Long getDeduct() {
        return deduct;
    }

    public void setDeduct(Long deduct) {
        this.deduct = deduct;
    }

    @Column( name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column( name = "cost_type")
    public Integer getCostType() {
        return costType;
    }

    public void setCostType(Integer costType) {
        this.costType = costType;
    }

    @Column( name = "cdr_original")
    public String getCdrOriginal() {
        return cdrOriginal;
    }

    public void setCdrOriginal(String cdrOriginal) {
        this.cdrOriginal = cdrOriginal;
    }
}
