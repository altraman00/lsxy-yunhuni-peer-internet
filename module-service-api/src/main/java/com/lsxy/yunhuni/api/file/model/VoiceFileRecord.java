package com.lsxy.yunhuni.api.file.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/21.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_file_record")
public class VoiceFileRecord extends IdEntity {
    public static final String[] types = new String[]{"语音回拔","自定义IVR","语音会议","呼叫中心"};
    public final static int IS_DELETED_TRUE = 1;
    private String tenantId;//所属租户
    private String appId;//所属应用
    private String areaId; //录音所在区域
    private String name;//文件名 生成规则uuid
    private String url;//录音文件URL
    private String ossUrl;//录音文件URL
    private Long size;//文件大小
    private String sessionId;//所属会话 会议-id双向回拔-idivr id呼叫中心-交谈id
    private String sessionCode;//会话类型
    private Integer status; //文件是否已同步:1已同步0未同步-1同步失败
    private Integer ossDeleted;//oss文件删除状态 1 已删除，-1删除失败
    private Integer aaDeleted;//区域文件删除状态 1 已删除，-1删除失败
    private BigDecimal cost;//消费金额
    private Long callTimeLong;//呼叫时长
    private Long costTimeLong;//计费时长
    @Column(name="cost")
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    @Column(name="call_time_long")
    public Long getCallTimeLong() {
        return callTimeLong;
    }
    public void setCallTimeLong(Long callTimeLong) {
        this.callTimeLong = callTimeLong;
    }
    @Column(name="cost_time_long")
    public Long getCostTimeLong() {
        return costTimeLong;
    }

    public void setCostTimeLong(Long costTimeLong) {
        this.costTimeLong = costTimeLong;
    }

    @Column(name="oss_deleted")
    public Integer getOssDeleted() {
        return ossDeleted;
    }

    public void setOssDeleted(Integer ossDeleted) {
        this.ossDeleted = ossDeleted;
    }
    @Column(name="aa_deleted")
    public Integer getAaDeleted() {
        return aaDeleted;
    }

    public void setAaDeleted(Integer aaDeleted) {
        this.aaDeleted = aaDeleted;
    }

    @Column(name="tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name="app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name="area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name="url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Column(name="size")
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="session_id")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Column(name="oss_url")
    public String getOssUrl() {
        return ossUrl;
    }

    public void setOssUrl(String ossUrl) {
        this.ossUrl = ossUrl;
    }

    @Column(name="session_code")
    public String getSessionCode() {
        return sessionCode;
    }

    public void setSessionCode(String sessionCode) {
        this.sessionCode = sessionCode;
    }

    @Column(name="status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
