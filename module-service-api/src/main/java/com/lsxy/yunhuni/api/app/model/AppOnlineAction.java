package com.lsxy.yunhuni.api.app.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 应用上线动作
 * Created by liups on 2016/7/15.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_app_online_action")
public class AppOnlineAction extends IdEntity {
    public static final int TYPE_ONLINE = 1;  //上线
    public static final int TYPE_OFFLINE = 2; //下线

    public static final int ACTION_SELECT_NUM = 11; //选号
//    public static final int ACTION_PAYING = 12;     //支付
//    public static final int ACTION_CANCEL_PAY = 13; //上线取消（支付返回选号）
    public static final int ACTION_ONLINE = 14;     //上线完成

    public static final int ACTION_OFFLINE = 21;    //下线

    public static final int PAY_STATUS_PAID = 1;
    public static final int PAY_STATUS_NOPAID = 2;
    /*
    一个应用进行中的动作只能有一个
     */
    public static final int STATUS_AVTIVE = 1;    //进行中
    public static final int STATUS_DONE = 2;    //完成

    private String telNumber;                 //绑定IVR号
    private Integer payStatus;          //支付状态
    private BigDecimal amount;          //支付金额
    private App app;                    //应用
    private String areaId;
    private Integer type;               //1、上线 2、下线
    private Integer action;             //上线动作（操作动作：11 选号，12支付，13上线取消（支付返回选号），14上线完成）下线动作（操作动作：21 下线）
    private Integer status;             //1 进行中，2 已完成
    private String sipId;

    public AppOnlineAction() {
    }

    public AppOnlineAction(App app, Integer type, Integer action, Integer status) {
        this.app = app;
        this.type = type;
        this.action = action;
        this.status = status;
    }

    public AppOnlineAction(String telNumber, Integer payStatus, BigDecimal amount, App app, String areaId,String sipId,Integer type, Integer action, Integer status) {
        this.telNumber = telNumber;
        this.payStatus = payStatus;
        this.amount = amount;
        this.app = app;
        this.areaId = areaId;
        this.sipId = sipId;
        this.type = type;
        this.action = action;
        this.status = status;
    }

    @Column(name = "tel_number ")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Column(name = "pay_status")
    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Column(name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Column(name = "action")
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "sip_id")
    public String getSipId() {
        return sipId;
    }

    public void setSipId(String sipId) {
        this.sipId = sipId;
    }
}
