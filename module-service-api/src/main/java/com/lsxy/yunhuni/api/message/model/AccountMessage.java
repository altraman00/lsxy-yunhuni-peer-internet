package com.lsxy.yunhuni.api.message.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.message.model.Message;
import com.lsxy.framework.api.tenant.model.Account;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/4.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_account_message")
public class AccountMessage extends IdEntity {
    public static final String MESSAGE_TYPE_AUTH_ONESELE_SUCCESS = "message-templates/01_message_type_auth_onesele_success.vm";
    public static final String MESSAGE_TYPE_AUTH_ONESELE_FAIL = "message-templates/02_message_type_auth_onesele_fail.vm";
    public static final String MESSAGE_TYPE_AUTH_COMPANY_SUCCESS = "message-templates/03_message_type_auth_company_success.vm";
    public static final String MESSAGE_TYPE_AUTH_COMPANY_FAIL = "message-templates/04_message_type_auth_company_fail.vm";
    public static final String MESSAGE_TYPE_VOICE_PLAY_SUCCESS = "message-templates/05_message_type_voice_play_success.vm";
    public static final String MESSAGE_TYPE_VOICE_PLAY_FAIL = "message-templates/06_message_type_voice_play_fail.vm";
    public static final String MESSAGE_TYPE_INVOCE_APPLY_SUCCESS = "message-templates/07_message_type_invoce_apply_success.vm";
    public static final String MESSAGE_TYPE_INVOCE_APPLY_FAIL = "message-templates/08_message_type_invoce_apply_fail.vm";
    public static final String MESSAGE_TYPE_ARREARS= "message-templates/09_message_type_arrears.vm";
    public static final String MESSAGE_TYPE_FEEDBACK= "message-templates/10_message_type_feedback.vm";
    public static final String MESSAGE_TYPE_APP_FAIL= "message-templates/11_message_type_app_fail.vm";
    public static final String MESSAGE_TYPE_APP_SUCCESS= "message-templates/12_message_type_app_success.vm";
    public static final String MESSAGE_MSG_TEMPLATE_FAIL= "message-templates/13_message_type_msg_template_fail.vm";
    public static final String MESSAGE_MSG_TEMPLATE_SUCCESS= "message-templates/14_message_type_msg_template_success.vm";
    public  static final int READ = 1;
    public static final int DELETE = -1;
    public static final int NOT = 0;
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
