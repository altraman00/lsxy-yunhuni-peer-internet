package com.lsxy.framework.tenant.model;

import com.lsxy.framework.core.persistence.IdEntity;
import org.aspectj.lang.annotation.control.CodeGenerationHint;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Set;

/**
 * 
 * 租户
 * 
 * @author tandy
 * 
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_tenant")
public class Tenant extends IdEntity{

	//是否实名
	private boolean isRealAuth;

	//租户识别码
	private String tenantUid;

	//账号
	private Set<Account> accounts;

	private Billing billing;

	@Column(name = "is_real_auth")
	public boolean isRealAuth() {
		return isRealAuth;
	}

	public void setRealAuth(boolean realAuth) {
		isRealAuth = realAuth;
	}

	@Column(name = "tid")
	public String getTenantUid() {
		return tenantUid;
	}

	public void setTenantUid(String tenantUid) {
		this.tenantUid = tenantUid;
	}

	@OneToMany(mappedBy="tenant")
	@LazyCollection(LazyCollectionOption.EXTRA)
	public Set<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}

	@OneToOne(mappedBy="tenant")
	public Billing getBilling() {
		return billing;
	}

	public void setBilling(Billing billing) {
		this.billing = billing;
	}
}
