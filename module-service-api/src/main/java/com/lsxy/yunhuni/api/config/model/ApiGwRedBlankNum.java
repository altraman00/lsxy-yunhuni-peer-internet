package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * api 号码红黑名单
 * Created by liups on 2016/8/23.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_config_num_redblacklist")
public class ApiGwRedBlankNum extends IdEntity {
    public static final int STATUS_ENABLED = 1;//启用状态
    public static final int STATUS_DISABLED = 2; //禁用状态

    public static final int TYPE_RED = 1;   //红名单
    public static final int TYPE_BLACK = 2;    //黑名单

    private String number;  //号码
    private Integer type;   //1红名单，2黑名单
    private Integer status; //状态:1启用2禁用
    private String remark;  //备注

    public ApiGwRedBlankNum() {
    }

    public ApiGwRedBlankNum(String number, Integer type) {
        this.number = number;
        this.type = type;
        this.status = STATUS_ENABLED;
    }

    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
