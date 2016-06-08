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
	private String name;
	private String id;
	private String type;
	private String tenantUn;
	private String tenantName;
	private String number;
	private String userName;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}


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



	public SecurityUser(String id, String userName, String name, String tenantUn,String tenantName) {
		this.userName = userName;
		this.name = name;
		this.id = id;
		this.tenantUn = tenantUn;
		this.tenantName = tenantName;
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

}
