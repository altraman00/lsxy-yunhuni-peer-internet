package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.api.base.IdEntity;

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

	//是否实名
	private boolean isRealAuth;

	//租户识别码
	private String tenantUid;

	public boolean isRealAuth() {
		return isRealAuth;
	}

	public void setRealAuth(boolean realAuth) {
		isRealAuth = realAuth;
	}

	public String getTenantUid() {
		return tenantUid;
	}

	public void setTenantUid(String tenantUid) {
		this.tenantUid = tenantUid;
	}
}
