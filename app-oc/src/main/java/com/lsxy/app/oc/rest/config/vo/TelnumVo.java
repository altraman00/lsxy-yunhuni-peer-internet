package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by zhangxb on 2016/10/26.
 */
@ApiModel
public class TelnumVo {
    @ApiModelProperty(name="telNumber",value = "号码")
    private String telNumber;
    @ApiModelProperty(name="callUri",value = "呼出URI")
    private String callUri;
    @ApiModelProperty(name="operator",value = "运营商")
    private String operator;
    @ApiModelProperty(name="amount",value = "号码占用费")
    private String amount ;
    @ApiModelProperty(name="isDialing",value = "可主叫，1是0否")
    private int isDialing;
    @ApiModelProperty(name="isCalled",value = "可被叫，1是0否")
    private int isCalled;
//    @ApiModelProperty(name="isThrough",value = "是否可透传，1是0否")
//    private int isThrough;
    @ApiModelProperty(name="areaCode",value = "归属地区号")
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getCallUri() {
        return callUri;
    }

    public void setCallUri(String callUri) {
        this.callUri = callUri;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(int isDialing) {
        this.isDialing = isDialing;
    }

    public int getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(int isCalled) {
        this.isCalled = isCalled;
    }

//    public int getIsThrough() {
//        return isThrough;
//    }
//
//    public void setIsThrough(int isThrough) {
//        this.isThrough = isThrough;
//    }
}
