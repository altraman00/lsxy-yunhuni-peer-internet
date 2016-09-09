package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;

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
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_cdr")
public class VoiceCdr extends IdEntity {
    public static final Integer COST_TYPE_DEDUCT = 1;
    public static final Integer COST_TYPE_COST = 2;
    public static final Integer COST_TYPE_COST_DEDUCT = 3;
    private String sessionId;//会话ID
    private String areaId;//所属区域
    private String tenantId;//所属租户
    private String  appId;//所属应用
    private String lineId;//所属线路
    private String type;//'查看产品表code字段或枚举类ProductCode
    private String relevanceId;//根据会话类型关联对应类型的表的记录
    private String recordUrl;//录音文件URL
    private Long recordSize;//录音文件大小
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
    private String cdr_id;
    private String cdr_nodeid;
    private String cdr_cdrid ;
    private String cdr_processid;
    private String cdr_callid;
    private String cdr_ch;
    private String cdr_devno;
    private String cdr_ani;
    private String cdr_dnis;
    private String cdr_dnis2;
    private String cdr_orgcallno;
    private String cdr_dir;
    private String cdr_devtype;
    private String cdr_busitype;
    private String cdr_callstatus;
    private String cdr_endtype;
    private String cdr_ipscreason;
    private String cdr_callfailcause;
    private String cdr_callbegintime;
    private String cdr_connectbegintime;
    private String cdr_callendtime;
    private String cdr_talkduration;
    private String cdr_projectid;
    private String cdr_flowid;
    private String cdr_additionalinfo1;
    private String cdr_additionalinfo2;
    private String cdr_additionalinfo3;
    private String cdr_additionalinfo4;
    private String cdr_additionalinfo5;

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
    @Column( name = "record_url")
    public String getRecordUrl() {
        return recordUrl;
    }

    public void setRecordUrl(String recordUrl) {
        this.recordUrl = recordUrl;
    }
    @Column( name = "record_size")
    public Long getRecordSize() {
        return recordSize;
    }

    public void setRecordSize(Long recordSize) {
        this.recordSize = recordSize;
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

    @Column( name = "cdr_id")
    public String getCdr_id() {
        return cdr_id;
    }

    public void setCdr_id(String cdr_id) {
        this.cdr_id = cdr_id;
    }

    @Column( name = "cdr_nodeid")
    public String getCdr_nodeid() {
        return cdr_nodeid;
    }

    public void setCdr_nodeid(String cdr_nodeid) {
        this.cdr_nodeid = cdr_nodeid;
    }

    @Column( name = "cdr_cdrid")
    public String getCdr_cdrid() {
        return cdr_cdrid;
    }

    public void setCdr_cdrid(String cdr_cdrid) {
        this.cdr_cdrid = cdr_cdrid;
    }

    @Column( name = "cdr_processid")
    public String getCdr_processid() {
        return cdr_processid;
    }

    public void setCdr_processid(String cdr_processid) {
        this.cdr_processid = cdr_processid;
    }

    @Column( name = "cdr_callid")
    public String getCdr_callid() {
        return cdr_callid;
    }

    public void setCdr_callid(String cdr_callid) {
        this.cdr_callid = cdr_callid;
    }

    @Column( name = "cdr_ch")
    public String getCdr_ch() {
        return cdr_ch;
    }

    public void setCdr_ch(String cdr_ch) {
        this.cdr_ch = cdr_ch;
    }

    @Column( name = "cdr_devno")
    public String getCdr_devno() {
        return cdr_devno;
    }

    public void setCdr_devno(String cdr_devno) {
        this.cdr_devno = cdr_devno;
    }

    @Column( name = "cdr_ani")
    public String getCdr_ani() {
        return cdr_ani;
    }

    public void setCdr_ani(String cdr_ani) {
        this.cdr_ani = cdr_ani;
    }

    @Column( name = "cdr_dnis")
    public String getCdr_dnis() {
        return cdr_dnis;
    }

    public void setCdr_dnis(String cdr_dnis) {
        this.cdr_dnis = cdr_dnis;
    }

    @Column( name = "cdr_dnis2")
    public String getCdr_dnis2() {
        return cdr_dnis2;
    }

    public void setCdr_dnis2(String cdr_dnis2) {
        this.cdr_dnis2 = cdr_dnis2;
    }

    @Column( name = "cdr_orgcallno")
    public String getCdr_orgcallno() {
        return cdr_orgcallno;
    }

    public void setCdr_orgcallno(String cdr_orgcallno) {
        this.cdr_orgcallno = cdr_orgcallno;
    }

    @Column( name = "cdr_dir")
    public String getCdr_dir() {
        return cdr_dir;
    }

    public void setCdr_dir(String cdr_dir) {
        this.cdr_dir = cdr_dir;
    }

    @Column( name = "cdr_devtype")
    public String getCdr_devtype() {
        return cdr_devtype;
    }

    public void setCdr_devtype(String cdr_devtype) {
        this.cdr_devtype = cdr_devtype;
    }

    @Column( name = "cdr_busitype")
    public String getCdr_busitype() {
        return cdr_busitype;
    }

    public void setCdr_busitype(String cdr_busitype) {
        this.cdr_busitype = cdr_busitype;
    }

    @Column( name = "cdr_callstatus")
    public String getCdr_callstatus() {
        return cdr_callstatus;
    }

    public void setCdr_callstatus(String cdr_callstatus) {
        this.cdr_callstatus = cdr_callstatus;
    }

    @Column( name = "cdr_endtype")
    public String getCdr_endtype() {
        return cdr_endtype;
    }

    public void setCdr_endtype(String cdr_endtype) {
        this.cdr_endtype = cdr_endtype;
    }

    @Column( name = "cdr_ipscreason")
    public String getCdr_ipscreason() {
        return cdr_ipscreason;
    }

    public void setCdr_ipscreason(String cdr_ipscreason) {
        this.cdr_ipscreason = cdr_ipscreason;
    }

    @Column( name = "cdr_callfailcause")
    public String getCdr_callfailcause() {
        return cdr_callfailcause;
    }

    public void setCdr_callfailcause(String cdr_callfailcause) {
        this.cdr_callfailcause = cdr_callfailcause;
    }

    @Column( name = "cdr_callbegintime")
    public String getCdr_callbegintime() {
        return cdr_callbegintime;
    }

    public void setCdr_callbegintime(String cdr_callbegintime) {
        this.cdr_callbegintime = cdr_callbegintime;
    }

    @Column( name = "cdr_connectbegintime")
    public String getCdr_connectbegintime() {
        return cdr_connectbegintime;
    }

    public void setCdr_connectbegintime(String cdr_connectbegintime) {
        this.cdr_connectbegintime = cdr_connectbegintime;
    }

    @Column( name = "cdr_callendtime")
    public String getCdr_callendtime() {
        return cdr_callendtime;
    }

    public void setCdr_callendtime(String cdr_callendtime) {
        this.cdr_callendtime = cdr_callendtime;
    }

    @Column( name = "cdr_talkduration")
    public String getCdr_talkduration() {
        return cdr_talkduration;
    }

    public void setCdr_talkduration(String cdr_talkduration) {
        this.cdr_talkduration = cdr_talkduration;
    }

    @Column( name = "cdr_projectid")
    public String getCdr_projectid() {
        return cdr_projectid;
    }

    public void setCdr_projectid(String cdr_projectid) {
        this.cdr_projectid = cdr_projectid;
    }

    @Column( name = "cdr_flowid")
    public String getCdr_flowid() {
        return cdr_flowid;
    }

    public void setCdr_flowid(String cdr_flowid) {
        this.cdr_flowid = cdr_flowid;
    }

    @Column( name = "cdr_additionalinfo1")
    public String getCdr_additionalinfo1() {
        return cdr_additionalinfo1;
    }

    public void setCdr_additionalinfo1(String cdr_additionalinfo1) {
        this.cdr_additionalinfo1 = cdr_additionalinfo1;
    }

    @Column( name = "cdr_additionalinfo2")
    public String getCdr_additionalinfo2() {
        return cdr_additionalinfo2;
    }

    public void setCdr_additionalinfo2(String cdr_additionalinfo2) {
        this.cdr_additionalinfo2 = cdr_additionalinfo2;
    }

    @Column( name = "cdr_additionalinfo3")
    public String getCdr_additionalinfo3() {
        return cdr_additionalinfo3;
    }

    public void setCdr_additionalinfo3(String cdr_additionalinfo3) {
        this.cdr_additionalinfo3 = cdr_additionalinfo3;
    }

    @Column( name = "cdr_additionalinfo4")
    public String getCdr_additionalinfo4() {
        return cdr_additionalinfo4;
    }

    public void setCdr_additionalinfo4(String cdr_additionalinfo4) {
        this.cdr_additionalinfo4 = cdr_additionalinfo4;
    }

    @Column( name = "cdr_additionalinfo5")
    public String getCdr_additionalinfo5() {
        return cdr_additionalinfo5;
    }

    public void setCdr_additionalinfo5(String cdr_additionalinfo5) {
        this.cdr_additionalinfo5 = cdr_additionalinfo5;
    }
}
