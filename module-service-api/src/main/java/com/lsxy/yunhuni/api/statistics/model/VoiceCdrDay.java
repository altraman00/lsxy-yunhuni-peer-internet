package com.lsxy.yunhuni.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 通话记录统计（session统计）日统计
 * Created by zhangxb on 2016/8/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_cdr_day")
public class VoiceCdrDay extends IdEntity {
    private String tenantId;//所属租户
    private String appId;//所属应用
    private String type;//会话类型1.语音通知2.双向回拨3.会议4.IVR定制服务5.语音验证码6.录音
    private String operatorId;//操作类型
    private String areaId;//区域
    private Date dt;//统计时间
    private Integer day;//统计日期范围1-31
    private Long amongCostTime;//本时段消费会话时长
    private Long amongDuration;//本时段会话时长
    private Long amongConnect;//本时段接通个数
    private Long amongNotConnect;//本时段未接通个数
    private Long amongCall;//本时段总通话个数
    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "day")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }


    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "operator_id")
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    @Column(name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name = "among_cost_time")
    public Long getAmongCostTime() {
        return amongCostTime;
    }

    public void setAmongCostTime(Long amongCostTime) {
        this.amongCostTime = amongCostTime;
    }

    @Column(name = "among_duration")
    public Long getAmongDuration() {
        return amongDuration;
    }

    public void setAmongDuration(Long amongDuration) {
        this.amongDuration = amongDuration;
    }

    @Column(name = "among_connect")
    public Long getAmongConnect() {
        return amongConnect;
    }

    public void setAmongConnect(Long amongConnect) {
        this.amongConnect = amongConnect;
    }
    @Column(name = "among_not_connect")
    public Long getAmongNotConnect() {
        return amongNotConnect;
    }

    public void setAmongNotConnect(Long amongNotConnect) {
        this.amongNotConnect = amongNotConnect;
    }
    @Column(name = "among_call")
    public Long getAmongCall() {
        return amongCall;
    }

    public void setAmongCall(Long amongCall) {
        this.amongCall = amongCall;
    }
}
