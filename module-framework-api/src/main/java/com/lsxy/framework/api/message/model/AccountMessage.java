package com.lsxy.framework.api.message.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Account;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/4.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_account_message")
public class AccountMessage extends IdEntity{
    private Message message;//对于消息
    private Account account;//所属用户
    private int status;//消息状态 '0未处理;1已读;-1已删除'
    private Date createTime;//创建时间
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

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
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
