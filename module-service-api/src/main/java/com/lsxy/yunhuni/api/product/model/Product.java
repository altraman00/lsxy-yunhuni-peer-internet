package com.lsxy.yunhuni.api.product.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 产品表
 * Created by liups on 2016/8/27.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_yy_product")
public class Product extends IdEntity {



    public static int CAL_TYPE_NUM = 1;
    public static int CAL_TYPE_TIME = 2;

    private String name;    //产品名称
    private String code;    //一次写入，不可修改，涉及到编程
    private Integer calType;    //1、按数量，2、按时长
    private Integer timeUnit;   //单位时长(单位秒)
    private String unit;   //单位
    private String remark;

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "cal_type")
    public Integer getCalType() {
        return calType;
    }

    public void setCalType(Integer calType) {
        this.calType = calType;
    }

    @Column(name = "time_unit")
    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
