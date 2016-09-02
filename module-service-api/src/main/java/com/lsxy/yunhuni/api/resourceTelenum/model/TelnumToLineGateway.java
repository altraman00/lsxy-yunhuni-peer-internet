package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liups on 2016/9/2.
 */
@Entity
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_bi_yy_telnum_to_linegateway")
public class TelnumToLineGateway extends IdEntity {
    private String telNumber;
    private String lineId;
    private String provider;

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
