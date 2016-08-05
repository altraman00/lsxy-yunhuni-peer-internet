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
	private String id;
	private String userName;
	private String tid; //租户自增长ID，并不是租户的ID
	private String tenantId; //租户ID

	public SecurityUser() {
	}

	public SecurityUser(String id, String userName, String tid, String tenantId) {
		this.id = id;
		this.userName = userName;
		this.tid = tid;
		this.tenantId = tenantId;
	}
	public String getId() {
		return id;
	}

	public String getUserName() {
		return userName;
	}

	public String getTid() {
		return tid;
	}

	public String getTenantId() {
		return tenantId;
	}


	/**
	 */
	@Override
	public String toString() {
		return JSONUtil2.objectToJson(this);
	}


}
