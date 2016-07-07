package com.lsxy.framework.api.config.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Tandy on 2016/7/7.
 * api gateway 黑名单配置
 *
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_config_api_blackip")
public class ApiGwBlankIP  extends IdEntity{

    //启用状态
    public static final int ST_ENABLED=1;
    //禁用状态
    public static final int ST_DISABLED=2;

    private String ip;
    private int status;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
