package com.hesyun.core.security;

import java.util.Map;

import com.hesyun.core.SystemConfig;
import com.hesyun.core.utils.JSONUtil2Serialization;


public class SecurityUser implements JSONUtil2Serialization{
	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	private String loginName;
	private String name;
	private String id;
	private String type;
	private String tenantUn;
	private String roles;
	private String tenantName;
	private String roleNames;
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	private Map<String, Boolean> authMap;
	
	

	public String getTenantName() {
		return tenantName;
	}

	public String getTenantUn() {
		return tenantUn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	/**
	 * 是否超级管理员管理员
	 * @return
	 */
	public boolean getIsAdmin(){
		return this.getLoginName().equals(SystemConfig.getProperty("root.username"));
	}
	
	/**
	 * 是否为租户管理员
	 */
	public boolean getIsTenantAdmin() {
		return this.roles.indexOf("ROLE_TENANT_ADMIN")>=0;
	}

	/**
	 * 是否为和声云运营管理员
	 */
	public boolean isHsyOperator() {
		return this.roles.indexOf("ROLE_HSY_OPERATE")>=0;
	}


	public SecurityUser(String id, String loginName, String name, String tenantUn,String tenantName, String roles) {
		this.loginName = loginName;
		this.name = name;
		this.id = id;
		this.tenantUn = tenantUn;
		this.tenantName = tenantName;
		this.roles = roles;
	}
	


	public String getRoles() {
		return roles;
	}

	public String getLoginName() {
		return loginName;
	}

	/**
	 * 本函数输出将作为默认的<shiro:principal/>输出.
	 */
	@Override
	public String toString() {
		return loginName;
	}

	public String getName() {
		return name;
	}

	public Map<String, Boolean> getAuthMap() {
		return authMap;
	}

	public void setAuthMap(Map<String, Boolean> authMap) {
		this.authMap = authMap;
	}

	@Override
	public String toJson() {
		return null;
	}

	
}
