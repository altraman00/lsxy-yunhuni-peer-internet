package com.lsxy.framework.api.message.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Account;

import javax.persistence.*;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/4.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_account_message")
public class AccountMessage extends IdEntity {
    public static final String MESSAGE_TYPE_AUTH_ONESELE_SUCCESS = "01_message_type_auth_onesele_success.vm";
    public static final String MESSAGE_TYPE_AUTH_ONESELE_FAIL = "02_message_type_auth_onesele_fail.vm";
    public static final String MESSAGE_TYPE_AUTH_COMPANY_SUCCESS = "03_message_type_auth_company_success.vm";
    public static final String MESSAGE_TYPE_AUTH_COMPANY_FAIL = "04_message_type_auth_company_fail.vm";
    public static final String MESSAGE_TYPE_VOICE_PLAY_SUCCESS = "05_message_type_voice_play_success.vm";
    public static final String MESSAGE_TYPE_VOICE_PLAY_FAIL = "06_message_type_voice_play_fail.vm";
    public static final String MESSAGE_TYPE_INVOCE_APPLY_SUCCESS = "07_message_type_invoce_apply_success.vm";
    public static final String MESSAGE_TYPE_INVOCE_APPLY_FAIL = "08_message_type_invoce_apply_fail.vm";
    public static final String MESSAGE_TYPE_ARREARS= "09_message_type_arrears.vm";
    public static final String MESSAGE_TYPE_FEEDBACK= "10_message_type_feedback.vm";
    public  static final Integer READ = 1;
    public static final Integer DELETE = -1;
    public static final Integer NOT = 0;
    private Message message;//对于消息
    private Account account;//所属用户
    private Integer status;//消息状态 '0未处理;1已读;-1已删除'

    @ManyToOne
    @JoinColumn(name = "message_id")
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    @ManyToOne
    @JoinColumn(name = "account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
