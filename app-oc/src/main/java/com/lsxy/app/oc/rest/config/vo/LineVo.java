package com.lsxy.app.oc.rest.config.vo;

import com.lsxy.yunhuni.api.config.model.LineGateway;

/**
 * Created by zhangxb on 2016/11/3.
 */
public class LineVo {
    private String telNumber;//号码
    private String lineId;//'所属线路网关'
    private String provider;//'供应商'
    private String isDialing;//可主叫
    private String isCalled;//可被叫
    private String isThrough;//可透传
    private String isBuy;//是否采购线路 1是 0否
    private LineGateway lineGateway;

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

    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }

    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }

    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
    }

    public String getIsBuy() {
        return isBuy;
    }

    public void setIsBuy(String isBuy) {
        this.isBuy = isBuy;
    }

    public LineGateway getLineGateway() {
        return lineGateway;
    }

    public void setLineGateway(LineGateway lineGateway) {
        this.lineGateway = lineGateway;
    }
}
