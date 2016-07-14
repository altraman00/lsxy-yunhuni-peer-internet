package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.api.base.IdEntity;

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
public class Tenant extends IdEntity {
	public   static final Integer AUTH_NO = 100;//未认证
	public  static final Integer AUTH_COMPANY_SUCESS = 2;//企业认证成功
	public  static final Integer AUTH_ONESELF_SUCESS = 1;//个人认证成功

	private static final long serialVersionUID = 1L;
	//是否实名
	private int isRealAuth;

	//租户识别码
	private String tenantUid;

	private String registerUserId; //注册的账号ID

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

	@Column(name = "reg_user_id")
	public String getRegisterUserId() {
		return registerUserId;
	}

	public void setRegisterUserId(String registerUserId) {
		this.registerUserId = registerUserId;
	}
}
