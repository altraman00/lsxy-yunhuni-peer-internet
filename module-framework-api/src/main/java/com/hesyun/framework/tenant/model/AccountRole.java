package com.hesyun.framework.tenant.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;
import com.hesyun.framework.sys.model.Role;

@Entity
@Table(schema="hsy_service_tenant",name = "tb_tenant_account_role")
@Where(clause = "deleted=0")
public class AccountRole extends IdEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Account person;
	
	private Role role;

	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="user_id")
	public Account getPerson() {
		return person;
	}

	public void setPerson(Account person) {
		this.person = person;
	}

	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="role_id")
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
