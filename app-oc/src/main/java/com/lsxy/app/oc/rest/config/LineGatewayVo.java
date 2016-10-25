package com.lsxy.app.oc.rest.config;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by zhangxb on 2016/10/24.
 */
@ApiModel
public class LineGatewayVo {
    @ApiModelProperty(name="isPublicLine",value = "1:全局线路;0:租户专属线路")
    private String isPublicLine;
    @ApiModelProperty(name="tenantId",value = "所属租户")
    private String tenantId;
    @ApiModelProperty(name="lineNumber",value = "线路标识")
    private String lineNumber;
    @ApiModelProperty(name="operator",value = "运营商 CHINATELECOM=中国电信；CHINAMOBILE=中国移动；CHINAUNICOM=中国联通")
    private String operator;
    @ApiModelProperty(name="areaId",value = "区域编号")
    private String areaId;
    @ApiModelProperty(name="lineType",value = "线路类型")
    private String lineType;
    @ApiModelProperty(name="sipProviderIp",value = "IP")
    private String sipProviderIp;
    @ApiModelProperty(name="sipProviderPort",value = "端口")
    private String sipProviderPort;
    @ApiModelProperty(name="sipAuthPassword",value = "domain")
    private String sipProviderDomain;
    @ApiModelProperty(name="lineType",value = "鉴权方式")
    private String sipAuthType;
    @ApiModelProperty(name="sipAuthAccount",value = "账号")
    private String sipAuthAccount;
    @ApiModelProperty(name="sipAuthPassword",value = "密码")
    private String sipAuthPassword;
    @ApiModelProperty(name="areaCode",value = "归属地区号")
    private String areaCode;
    @ApiModelProperty(name="fromPrefix",value = "呼入主叫前缀")
    private String fromPrefix;
    @ApiModelProperty(name="fromPrefix",value = "手机区号规则：0=全部加0；1=全部不加0；2:=被叫归属地与线路归属地不一致，加0")
    private String mobileAreaRule;
    @ApiModelProperty(name="fromPrefix",value = "固话区号规则：0=一律加区号；1=一律不加区号；2=非与线路属于同一个归属地加区号")
    private String telAreaRule;
    @ApiModelProperty(name="isThrough",value = "是否透传：1=是，0=不是")
    private String isThrough;
    @ApiModelProperty(name="lingPrice",value = "成本价")
    private BigDecimal lingPrice;
    @ApiModelProperty(name="quality",value = "质量：数字1-10")
    private Integer quality;
    @ApiModelProperty(name="priority",value = "手动优先级：数字0-99")
    private Integer priority;
    @ApiModelProperty(name="capacity",value = "并发容量：数字")
    private Integer capacity;

    public String getisThrough() {
        return isThrough;
    }

    public void setisThrough(String isThrough) {
        this.isThrough = isThrough;
    }

    public String getIsPublicLine() {
        return isPublicLine;
    }

    public void setIsPublicLine(String isPublicLine) {
        this.isPublicLine = isPublicLine;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public String getSipProviderIp() {
        return sipProviderIp;
    }

    public void setSipProviderIp(String sipProviderIp) {
        this.sipProviderIp = sipProviderIp;
    }

    public String getSipProviderPort() {
        return sipProviderPort;
    }

    public void setSipProviderPort(String sipProviderPort) {
        this.sipProviderPort = sipProviderPort;
    }

    public String getSipProviderDomain() {
        return sipProviderDomain;
    }

    public void setSipProviderDomain(String sipProviderDomain) {
        this.sipProviderDomain = sipProviderDomain;
    }

    public String getSipAuthType() {
        return sipAuthType;
    }

    public void setSipAuthType(String sipAuthType) {
        this.sipAuthType = sipAuthType;
    }

    public String getSipAuthAccount() {
        return sipAuthAccount;
    }

    public void setSipAuthAccount(String sipAuthAccount) {
        this.sipAuthAccount = sipAuthAccount;
    }

    public String getSipAuthPassword() {
        return sipAuthPassword;
    }

    public void setSipAuthPassword(String sipAuthPassword) {
        this.sipAuthPassword = sipAuthPassword;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getFromPrefix() {
        return fromPrefix;
    }

    public void setFromPrefix(String fromPrefix) {
        this.fromPrefix = fromPrefix;
    }

    public String getMobileAreaRule() {
        return mobileAreaRule;
    }

    public void setMobileAreaRule(String mobileAreaRule) {
        this.mobileAreaRule = mobileAreaRule;
    }

    public String getTelAreaRule() {
        return telAreaRule;
    }

    public void setTelAreaRule(String telAreaRule) {
        this.telAreaRule = telAreaRule;
    }

    public BigDecimal getLingPrice() {
        return lingPrice;
    }

    public void setLingPrice(BigDecimal lingPrice) {
        this.lingPrice = lingPrice;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
