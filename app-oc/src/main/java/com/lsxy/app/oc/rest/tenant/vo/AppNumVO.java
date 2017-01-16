package com.lsxy.app.oc.rest.tenant.vo;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by liups on 2017/1/11.
 */
public class AppNumVO {
    @ApiModelProperty(name = "rentId",value = "租用记录Id")
    String rentId;
    @ApiModelProperty(name = "num",value = "号码")
    String num;
    @ApiModelProperty(name = "status",value = "状态：0、过期，1、正常")
    String status;
    @ApiModelProperty(name = "isCalled",value = "是否可呼入")
    String isCalled;
    @ApiModelProperty(name = "isDialing",value = "是否可呼出")
    String isDialing;
    @ApiModelProperty(name = "areaCode",value = "区号")
    String areaCode;
    @ApiModelProperty(name = "expireTime",value = "过期时间")
    Date expireTime;

    public String getRentId() {
        return rentId;
    }

    public void setRentId(String rentId) {
        this.rentId = rentId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }

    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
