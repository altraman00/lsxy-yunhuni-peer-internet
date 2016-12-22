package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by liups on 2016/9/2.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_oc_telnum_to_linegateway")
public class TelnumToLineGateway extends IdEntity {
    public static final String ISDIALING_TRUE = "1";
    public static final String ISDIALING_FALSE = "0";
    public static final String ISCALLED_TRUE = "1";
    public static final String ISCALLED_FALSE = "0";
    public static final String ISTHROUGH_TRUE = "1";
    public static final String ISTHROUGH_FALSE = "0";

    private String telNumber;//号码
    private String lineId;//'所属线路网关'
    private String provider;//'供应商'
    private String isDialing;//可主叫
    private String isCalled;//可被叫
    private String isThrough;//可透传
    private String isBuy;//是否采购线路 1是 0否

    @Transient
    private ResourceTelenum resourceTelenum;

    @Column( name = "is_buy")
    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    @Column( name = "is_dialing")
    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }
    @Column( name = "is_called")
    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }
    @Column( name = "is_through")
    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
    }
    @Column( name = "tel_number")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
    @Column( name = "line_id")
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }
    @Column( name = "provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public TelnumToLineGateway() {
    }

    public TelnumToLineGateway(String telNumber, String lineId, String isDialing, String isCalled, String isThrough, String isBuy) {
        this.telNumber = telNumber;
        this.lineId = lineId;
        this.isDialing = isDialing;
        this.isCalled = isCalled;
        this.isThrough = isThrough;
        this.isBuy = isBuy;
    }

    @Transient
    public ResourceTelenum getResourceTelenum() {
        return resourceTelenum;
    }

    public void setResourceTelenum(ResourceTelenum resourceTelenum) {
        this.resourceTelenum = resourceTelenum;
    }
}
