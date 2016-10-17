package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liups on 2016/9/2.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_oc_telnum_to_linegateway")
public class TelnumToLineGateway extends IdEntity {
    private String telNumber;   //号码
    private String lineId;      //线路
    private String provider;
    private String isDialing;   //是否可主叫   0：否，1：是
    private String isCalled;    //是否可被叫    0：否，1：是
    private String isThrough;   //是否可透传     0：否，1：是

    @Column(name = "tel_number")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Column(name = "line_id")
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Column(name = "provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Column(name = "is_dialing")
    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }

    @Column(name = "is_called")
    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }

    @Column(name = "is_through")
    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
    }
}
