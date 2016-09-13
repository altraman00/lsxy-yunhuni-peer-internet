package com.lsxy.framework.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * api调用小时统计
 * Created by zhangxb on 2016/8/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_api_call_day")
public class ApiCallDay extends IdEntity {
    private String tenantId;//所属租户
    private String appId;//所属应用
    private Integer type;//api类型
    private Date dt;//统计时间
    private Integer day;//统计日期范围1-31
    private Long amongApi;//本时段api调用次数
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
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name = "among_api")
    public Long getAmongApi() {
        return amongApi;
    }

    public void setAmongApi(Long amongApi) {
        this.amongApi = amongApi;
    }
}
