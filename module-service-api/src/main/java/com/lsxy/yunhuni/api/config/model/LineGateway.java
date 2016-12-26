package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
    public final static String STATUS_USABLE = "1";
    public final static String STATUS_UNUSABLE = "0";
    public final static String MOBILEAREARULE_ADD_ZERO = "0";
    public final static String MOBILEAREARULE_NOT_ADD_ZERO = "1";
    public final static String MOBILEAREARULE_ADD_ZERO_WHEN_EQ = "2";
    public final static String TELAREARULE_ADD_CODE = "0";
    public final static String TELAREARULE_ADD_CODE_WHEN_EQ = "2";
    public final static String ISTHROUGH_TRUE = "1";
    public final static String ISTHROUGH_FALSE = "0";
    public final static String ISPUBLICLINE_TRUE = "1";
    public final static String ISPUBLICLINE_FALSE = "0";

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
    private String isThrough;//是否透传 1为可透传，0为不可透传
    private Integer quality;//质量 数字1-10
    private Integer capacity;//容量 数字
    private String remark;
    private String sipAuthIp;//sip接入点的外网IP地址
    private String status;//状态 1为可用，0为不可用
    private String isPublicLine;//是否全局线路

    @Transient
    private Integer priority;//优先级，临时数据，数据库不保存

    @Column(name="is_through")
    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
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
    public BigDecimal getLinePrice() {
        return linePrice;
    }

    public void setLinePrice(BigDecimal linePrice) {
        this.linePrice = linePrice;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    @Transient
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}