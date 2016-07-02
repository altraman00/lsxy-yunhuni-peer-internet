package com.lsxy.yuhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 全局号码资源
 * Created by zhangxb on 2016/7/1.
 */
@Entity
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_bi_yy_resource_telenum")
public class ResourceTelenum extends IdEntity{
    private int status;//1:已被租用 0:未被租用
    private String telNumber;//号码

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    @Column(name = "tel_number")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
}
