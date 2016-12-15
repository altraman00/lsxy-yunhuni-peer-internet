package com.lsxy.yunhuni.api.config.model;

import javax.persistence.*;
import java.io.Serializable;
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
@SqlResultSetMapping(name="lineGatewayVO",
        entities= {
                @EntityResult(
                        entityClass=LineGatewayVO.class,
                        fields= {
                                @FieldResult(name="id", column="id"),
                                @FieldResult(name="lineNumber", column="line_number"),
                                @FieldResult(name="operator", column="operator"),
                                @FieldResult(name="areaId", column="area_id"),
                                @FieldResult(name="areaCode", column="area_code"),
                                @FieldResult(name="fromPrefix", column="from_prefix"),
                                @FieldResult(name="lineType", column="line_type"),
                                @FieldResult(name="sipProviderIp", column="sip_provider_ip"),
                                @FieldResult(name="sipProviderDomain", column="sip_provider_domain"),
                                @FieldResult(name="sipAuthType", column="sip_auth_type"),
                                @FieldResult(name="sipAuthAccount", column="sip_auth_account"),
                                @FieldResult(name="sipAuthPassword", column="sip_auth_password"),
                                @FieldResult(name="mobileAreaRule", column="mobile_area_rule"),
                                @FieldResult(name="telAreaRule", column="tel_area_rule"),
                                @FieldResult(name="linePrice", column="line_price"),
                                @FieldResult(name="quality", column="quality"),
                                @FieldResult(name="capacity", column="capacity"),
                                @FieldResult(name="sipAuthIp", column="sip_auth_ip"),
                                @FieldResult(name="priority", column="priority")
                        })
        }
)
public class LineGatewayVO implements Serializable {
    private String id;
    private String lineNumber;  //线路标识
    private String operator;//运营商 中国电信；中国移动；中国联通
    private String areaId;//区域编号
    private String areaCode;//归属地区号
    private String fromPrefix;//呼入主叫前缀
    private String lineType;//线路类型 默认SIP
    private String sipProviderIp;//IP端口
    private String sipProviderDomain;   //线路网关域名
    private String sipAuthType;//鉴权方式1:账号密码 2:IP地址
    private String sipAuthAccount;//账号
    private String sipAuthPassword;//密码
    private String mobileAreaRule;//手机区号规则 0=全部加0；1=全部不加0；2:=被叫归属地与线路归属地不一致，加0
    private String telAreaRule;//固话区号规则 0=一律加区号；1=一律不加区号；2=非与线路属于同一个归属地加区号
    private BigDecimal linePrice;//线路网关单价
    private Integer quality;//质量 数字1-10
    private Integer capacity;//容量 数字
    private String sipAuthIp;//sip接入点的外网IP地址
    private Integer priority;//优先级，临时数据，数据库不保存

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSipProviderDomain() {
        return sipProviderDomain;
    }

    public void setSipProviderDomain(String sipProviderDomain) {
        this.sipProviderDomain = sipProviderDomain;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public BigDecimal getLinePrice() {
        return linePrice;
    }

    public void setLinePrice(BigDecimal linePrice) {
        this.linePrice = linePrice;
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

    public String getSipAuthIp() {
        return sipAuthIp;
    }

    public void setSipAuthIp(String sipAuthIp) {
        this.sipAuthIp = sipAuthIp;
    }

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}