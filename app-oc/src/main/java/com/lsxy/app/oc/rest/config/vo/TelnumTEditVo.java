package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by zhangxb on 2016/10/26.
 */
@ApiModel
public class TelnumTEditVo {
    @ApiModelProperty(name="tenantId",value = "所属租户")
    private String tenantId;
//    @ApiModelProperty(name="tenantId",value = "所属线路")
//    private String lineId;
    @ApiModelProperty(name="operator",value = "运营商 中国电信；中国移动；中国联通")
    private String operator;
    @ApiModelProperty(name="areaCode",value = "归属地区号")
    private String areaCode;
//    @ApiModelProperty(name="isDialing",value = "可主叫，1是0否")
//    private int isDialing;
//    @ApiModelProperty(name="isCalled",value = "可被叫，1是0否")
//    private int isCalled;
//    @ApiModelProperty(name="isThrough",value = "是否可透传，1是0否")
//    private int isThrough;
    @ApiModelProperty(name="callUri",value = "呼出URI")
    private String callUri;
    @ApiModelProperty(name="telNumber",value = "号码")
    private String telNumber;
    @ApiModelProperty(name="amount",value = "号码占用费")
    private BigDecimal amount ;



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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

}
