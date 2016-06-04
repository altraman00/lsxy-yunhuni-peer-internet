package com.lsxy.framework.core.security;

import java.io.Serializable;
import java.util.Map;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil2;


public class SecurityUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private String userName;

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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


	public SecurityUser(String id, String loginName, String name, String tenantUn,String tenantName, String roles, String userName) {
		this.loginName = loginName;
		this.name = name;
		this.id = id;
		this.tenantUn = tenantUn;
		this.tenantName = tenantName;
		this.roles = roles;
		this.userName = userName;
	}
	


	public String getRoles() {
		return roles;
	}

	public String getLoginName() {
		return loginName;
	}

	/**
	 */
	@Override
	public String toString() {
		return JSONUtil2.objectToJson(this);
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
}
