package com.hesyun.framework.sys.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.core.utils.JSONUtil;

/**
 * 租户角色
 * 
 * @author WangYun
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name = "tb_sys_role")
@Where(clause="deleted=0")
public class Role extends IdEntity  implements Serializable {
	
	
	private static final long serialVersionUID = 1L;

	public static final String ROLE_ADMIN = "ROLE_ADMIN";					//和声管理员
	public static final String ROLE_TENANT_ADMIN = "ROLE_TENANT_ADMIN";		//商户管理员角色
	public static final String ROLE_TENANT_OPERATOR="ROLE_TENANT_OPERATOR";	//普通帐号
	public static final String ROLE_TENANT_CW="ROLE_TENANT_CW";				//财务
	public static final String ROLE_TENANT_GYS = "ROLE_TENANT_GYS";			//供应商
	public static final String ROLE_TENANT_DLS="ROLE_TENANT_DLS";			//代理商

	// 角色编码
	private String roleCode;
	
	// 角色名称
	private String roleName;
	// 说明
	private String remark;
	
	public static String[] toJsonProperties = new String[] { "id", "roleName", "roleCode",
		"isFixed", "remark", "tenant","tenantUn","tenantUid","tenantName", "permissions", "modulePath", "name"};

	public String toJson() {
		return JSONUtil.objectToJson(this, toJsonProperties);
	}
	
	
	@Column(name="role_name")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}


	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="role_code")
	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	
}
