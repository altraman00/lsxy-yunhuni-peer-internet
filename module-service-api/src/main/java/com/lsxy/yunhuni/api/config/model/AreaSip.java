package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liups on 2016/11/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_config_area_sip")
public class AreaSip extends IdEntity {
    private String areaId;
    private String registrarIp;
    private String registrarPort;
    private String remark;

    @Column(name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name = "registrar_ip")
    public String getRegistrarIp() {
        return registrarIp;
    }

    public void setRegistrarIp(String registrarIp) {
        this.registrarIp = registrarIp;
    }

    @Column(name = "registrar_port")
    public String getRegistrarPort() {
        return registrarPort;
    }

    public void setRegistrarPort(String registrarPort) {
        this.registrarPort = registrarPort;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
