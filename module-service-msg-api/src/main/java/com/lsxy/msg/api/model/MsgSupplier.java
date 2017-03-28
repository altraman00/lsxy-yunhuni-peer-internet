package com.lsxy.msg.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created by liups on 2017/3/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_msg_supplier")
public class MsgSupplier extends IdEntity {
    public static final String QixuntongCode = "QiXunTong";
    public static final String PaopaoyuCode = "PaoPaoYu";
    public static final String JiMuCode = "JiMu";
    public static final int IS_MASS = 2;
    public static final int IS_SINGLE = 1;
    public static final int IS_HYBRID = 3;
    public static final int IS_DISABLE = 0;
    private String supplierName;
    private String code;
    private BigDecimal unitPrice;
    private Boolean isUssd;//是否支持闪印
    private Integer ussdSendType;
    private Boolean isTemplate;//是否支持模板
    private Integer templateSendType;
    private Boolean isSms;//是否支持短信
    private Integer smsSendType;//
    private String operator;//供应商的名称
    private String param;
    private Boolean enabled;
    private String remark;
    @Column(name = "ussd_send_type")
    public Integer getUssdSendType() {
        return ussdSendType;
    }

    public void setUssdSendType(Integer ussdSendType) {
        this.ussdSendType = ussdSendType;
    }
    @Column(name = "template_send_type")
    public Integer getTemplateSendType() {
        return templateSendType;
    }

    public void setTemplateSendType(Integer templateSendType) {
        this.templateSendType = templateSendType;
    }
    @Column(name = "sms_send_type")
    public Integer getSmsSendType() {
        return smsSendType;
    }

    public void setSmsSendType(Integer smsSendType) {
        this.smsSendType = smsSendType;
    }

    @Column(name = "supplier_name")
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "unit_price")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Column(name = "is_ussd")
    public Boolean getUssd() {
        return isUssd;
    }

    public void setUssd(Boolean ussd) {
        isUssd = ussd;
    }

    @Column(name = "is_template")
    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    @Column(name = "is_sms")
    public Boolean getSms() {
        return isSms;
    }

    public void setSms(Boolean sms) {
        isSms = sms;
    }

    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "param")
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Column(name = "enabled")
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
