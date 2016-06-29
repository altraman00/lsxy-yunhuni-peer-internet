package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.core.persistence.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
	private static final long serialVersionUID = 1L;
	//是否实名
	private int isRealAuth;

	//租户识别码
	private String tenantUid;

	@Column(name = "is_real_auth")
	public int getIsRealAuth() {
		return isRealAuth;
	}

	public void setIsRealAuth(int isRealAuth) {
		this.isRealAuth = isRealAuth;
	}
	@Column(name = "tid")
	public String getTenantUid() {
		return tenantUid;
	}

	public void setTenantUid(String tenantUid) {
		this.tenantUid = tenantUid;
	}
}
