package com.hesyun.framework.sys.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2Serialization;

/**
 * 角色权限
 * @author tandy
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_sys_role_permission")
@Where(clause="deleted=0")
public class RolePermission extends IdEntity  implements JSONUtil2Serialization{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Role role;
	
	private Resource res;
	
	
	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="role_id")
	public Role getRole() {
		return role;
	}



	public void setRole(Role role) {
		this.role = role;
	}


	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name="res_id")
	public Resource getRes() {
		return res;
	}



	public void setRes(Resource res) {
		this.res = res;
	}

	@Override
	public String toJson() {
		return JSONUtil.objectToJson(this, new String[]{"id","role","res","roleName","resname"});
	}

}
