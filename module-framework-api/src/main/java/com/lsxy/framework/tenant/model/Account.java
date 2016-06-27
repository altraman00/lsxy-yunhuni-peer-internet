package com.lsxy.framework.tenant.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lsxy.framework.core.persistence.IdEntity;

import javax.persistence.*;

/**
 * 租户用户
 * @author tandy
 */
@Entity
@Table(schema = "db_lsxy_base", name = "tb_base_account")
public class Account extends IdEntity {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String userName;
	private Tenant tenant;                //所属租户
	private String mobile;                //移动手机
	private String email;                    //电子邮件
	private String password;            //密码
	private String mm;

	private int status;                    //账号状态			租户用户状态：0-未激活 1-LOCK 2-正常  3-异常

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "mm")
	@JsonIgnore
	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}




	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "password")
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}



	@ManyToOne
	@JoinColumn(name = "tenant_id")
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}





}
