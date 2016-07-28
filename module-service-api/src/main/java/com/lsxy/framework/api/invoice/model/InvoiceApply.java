package com.lsxy.framework.api.invoice.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 发票申请
 * Created by liups on 2016/7/21.
 */
@Entity
@Table(schema = "db_lsxy_base", name = "tb_base_invoice_apply")
public class InvoiceApply extends IdEntity {
    public static Integer STATUS_SUBMIT = 0;    //申请已提交
    public static Integer STATUS_DONE = 1;      //处理完成，发票已寄出
    public static Integer STATUS_EXCEPTION = 2; //异常，这种状态一般为用户填写的资料有误，运营中心驳回申请

    private BigDecimal amount;      //金额
    private Date start;             //开始时间
    private Date end;               //结束时间
    private Integer status;         //状态
    private String remark;          //备注
    private Date applyTime;          //申请时间
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
    private String expressCom;  //快递公司
    private String expressNo;  //快递单号
    private String qualificationUrl; //资格证书URL （图片）

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "start")
    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    @Column(name = "end")
    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "apply_time")
    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

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

    @Column(name = "express_com")
    public String getExpressCom() {
        return expressCom;
    }

    public void setExpressCom(String expressCom) {
        this.expressCom = expressCom;
    }

    @Column(name = "express_no")
    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    @Column(name = "qualification_url")
    public String getQualificationUrl() {
        return qualificationUrl;
    }

    public void setQualificationUrl(String qualificationUrl) {
        this.qualificationUrl = qualificationUrl;
    }
}
