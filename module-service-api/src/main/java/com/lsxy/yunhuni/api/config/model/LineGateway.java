package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 线路网关
 * Created by liups on 2016/8/24.
 * 运营商
 * 品质
 * 透传
 * 权值
 * 容量 并发
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_config_line_gateway")
public class LineGateway extends IdEntity {
    private static final String CHINATELECOM="CHINATELECOM";//中国电信
    private static final String CHINAMOBILE="CHINAMOBILE";//中国移动
    private static final String CHINAUNICOM="CHINAUNICOM";//中国联通
    private String isThrough;//是否透传
    private String tenantId;//所属租户
    private String areaId;          //区域
    private String lineNumber;  //线路网关编码
    private BigDecimal lingPrice;   //线路网关单价
    private String sipProviderDomain;   //线路网关域名
    private String remark;
    private Integer priority;//线路网关优先级
    private String areaCode;//归属区号
    private String fromPrefix;//呼入主叫前缀
    private String mobileAreaRule;//手机区号规则
    private String telAreaRule;//固话区号规则
    private String lineType;//默认sip
    private String sipProviderIp;//线路网关IP
    private String sipProviderPort;//线路网关端口
    private String sipAuthType;//鉴权方式
    private String sipAuthAccount;//账号
    private String sipAuthPassword;//密码
    private String sipAuthIp;//sip接入点的外网IP地址
    private Integer quality;//质量
    private String status;//状态
    private String operator;//运营商
    private Integer capacity;//容量
    private String isPublicLine;//是否全局线路
    @Column(name="is_through")
    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
    }

    @Column(name="tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "sip_provider_domain")
    public String getSipProviderDomain() {
        return sipProviderDomain;
    }

    public void setSipProviderDomain(String sipProviderDomain) {
        this.sipProviderDomain = sipProviderDomain;
    }

    @Column(name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name = "line_number")
    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Column(name = "line_price")
    public BigDecimal getLingPrice() {
        return lingPrice;
    }

    public void setLingPrice(BigDecimal lingPrice) {
        this.lingPrice = lingPrice;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "priority")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    @Column(name = "area_code")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    @Column(name = "from_prefix")
    public String getFromPrefix() {
        return fromPrefix;
    }

    public void setFromPrefix(String fromPrefix) {
        this.fromPrefix = fromPrefix;
    }
    @Column(name = "mobile_area_rule")
    public String getMobileAreaRule() {
        return mobileAreaRule;
    }

    public void setMobileAreaRule(String mobileAreaRule) {
        this.mobileAreaRule = mobileAreaRule;
    }
    @Column(name = "tel_area_rule")
    public String getTelAreaRule() {
        return telAreaRule;
    }

    public void setTelAreaRule(String telAreaRule) {
        this.telAreaRule = telAreaRule;
    }
    @Column(name = "line_type")
    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }
    @Column(name = "sip_provider_ip")
    public String getSipProviderIp() {
        return sipProviderIp;
    }

    public void setSipProviderIp(String sipProviderIp) {
        this.sipProviderIp = sipProviderIp;
    }
    @Column(name = "sip_provider_port")
    public String getSipProviderPort() {
        return sipProviderPort;
    }

    public void setSipProviderPort(String sipProviderPort) {
        this.sipProviderPort = sipProviderPort;
    }
    @Column(name = "sip_auth_type")
    public String getSipAuthType() {
        return sipAuthType;
    }

    public void setSipAuthType(String sipAuthType) {
        this.sipAuthType = sipAuthType;
    }
    @Column(name = "sip_auth_account")
    public String getSipAuthAccount() {
        return sipAuthAccount;
    }

    public void setSipAuthAccount(String sipAuthAccount) {
        this.sipAuthAccount = sipAuthAccount;
    }
    @Column(name = "sip_auth_password")
    public String getSipAuthPassword() {
        return sipAuthPassword;
    }

    public void setSipAuthPassword(String sipAuthPassword) {
        this.sipAuthPassword = sipAuthPassword;
    }
    @Column(name = "sip_auth_ip")
    public String getSipAuthIp() {
        return sipAuthIp;
    }

    public void setSipAuthIp(String sipAuthIp) {
        this.sipAuthIp = sipAuthIp;
    }
    @Column(name = "quality")
    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    @Column(name = "capacity")
    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    @Column(name = "is_public_line")
    public String getIsPublicLine() {
        return isPublicLine;
    }

    public void setIsPublicLine(String isPublicLine) {
        this.isPublicLine = isPublicLine;
    }
}
