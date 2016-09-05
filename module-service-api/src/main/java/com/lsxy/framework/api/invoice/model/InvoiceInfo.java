package com.lsxy.framework.api.invoice.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;

import javax.persistence.*;

/**
 * 发票信息
 * Created by liups on 2016/7/14.
 */
@Entity
@Table(schema = "db_lsxy_base", name = "tb_base_invoice_info")
public class InvoiceInfo extends IdEntity{
    public static final int TYPE_PERSON_GENERAL = 1; //个人增值税普通发票
    public static final int TYPE_COM_GENERAL = 2;   //企业增值税普通发票
    public static final int TYPE_COM_SPECIAL = 3;   // 企业增值税专用发票

    private Tenant tenant;          //租户
    private Integer type;            //发票类型
    private String title;             //发票抬头
    private String taxpayerNum;     //纳税人识别号
    private String bank;            //开户行
    private String bankAccount;     //银行账户
    private String regAddress;      //注册地址
    private String phone;           //企业电话
    private String receiveAddress;  //收取地址
    private String receivePeople;  //收件人
    private String receiveMobile;  //手机号
    private String qualificationUrl; //资格证书URL （图片）

    @OneToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "taxpayer_num")
    public String getTaxpayerNum() {
        return taxpayerNum;
    }

    public void setTaxpayerNum(String taxpayerNum) {
        this.taxpayerNum = taxpayerNum;
    }

    @Column(name = "bank")
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Column(name = "bank_account")
    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Column(name = "reg_address")
    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    @Column(name = "phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "receive_address")
    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    @Column(name = "receive_people")
    public String getReceivePeople() {
        return receivePeople;
    }

    public void setReceivePeople(String receivePeople) {
        this.receivePeople = receivePeople;
    }

    @Column(name = "receive_mobile")
    public String getReceiveMobile() {
        return receiveMobile;
    }

    public void setReceiveMobile(String receiveMobile) {
        this.receiveMobile = receiveMobile;
    }

    @Column(name = "qualification_url")
    public String getQualificationUrl() {
        return qualificationUrl;
    }

    public void setQualificationUrl(String qualificationUrl) {
        this.qualificationUrl = qualificationUrl;
    }
}
