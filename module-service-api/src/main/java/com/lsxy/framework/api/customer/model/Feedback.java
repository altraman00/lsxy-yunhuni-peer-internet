package com.lsxy.framework.api.customer.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 反馈意见
 * Created by zhangxb on 2016/7/4.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_base",name = "tb_base_customer_feedback")
public class Feedback extends IdEntity {
    public  static final Integer  READ = 1;
    public  static final Integer UNREAD = 0;
    private String content;// 内容
    private Integer status;//状态 '0未处理;1已处理',
    Tenant tenant;//所属租户
    Account account;//所属用户

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn( name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    @ManyToOne
    @JoinColumn( name = "account_id")
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
