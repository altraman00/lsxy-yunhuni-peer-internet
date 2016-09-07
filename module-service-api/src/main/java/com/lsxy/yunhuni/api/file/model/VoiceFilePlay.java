package com.lsxy.yunhuni.api.file.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;

import javax.persistence.*;
import java.util.Date;

/**
 * 放音文件
 * Created by zhangxb on 2016/7/21.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_file_play")
public class VoiceFilePlay extends IdEntity {
    public static int STATUS_FAIL = -1;
    public static int STATUS_WAIT = 0;
    public static int STATUS_SUCCESS =1;
    private Tenant tenant;//所属租户
    private App app;//所属应用
    private String name;//文件名
    private String fileKey;//fileKey 生成规则/play_voice/tenant_id/app_id/yyyyMMdd/uuid
    private String remark;//备注
    private String url;//放音文件URL
    private Long size;//文件大小
    private Integer status;//'审核状态0带审核，1审核通过，-1审核不通过'
    private String checker;//审核人
    private Date checkTime;//审核时间
    private String reason;//不通过原因
    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Column(name="file_key")
    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
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

    @Column(name="status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name="checker")
    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }
    @Column(name="check_time")
    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }
    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name="remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
