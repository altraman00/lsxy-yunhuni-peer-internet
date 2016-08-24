package com.lsxy.framework.api.config.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tandy on 2016/7/7.
 * api gateway 黑名单配置
 *
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_yy_config_ip_blacklist")
public class ApiGwBlankIP  extends IdEntity {

    //启用状态
    public static final int ST_ENABLED=1;
    //禁用状态
    public static final int ST_DISABLED=2;

    private String ip;
    private Integer status;
    private String remark;

    @Column(name = "ip")
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
