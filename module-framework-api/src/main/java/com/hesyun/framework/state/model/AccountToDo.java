package com.hesyun.framework.state.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;

/**
 * 用户待办
 * 
 * 
 */
@Entity
@Table(schema="hsy_service_tenant",name = "tb_sua_account_todo")
@Where(clause="deleted=0")
public class AccountToDo extends IdEntity{
	private static final long serialVersionUID = 1L;


	private String personId;	//用户ID
	
	private String personName;  //用户姓名
	
	private String personAccount; //用户账号
	
	private int status; 		//处理状态       0-未处理  1-已处理  2-忽略
	
	private Date dt;			//时间

	@Column(name="person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	@Column(name="person_name")
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}
	
	@Column(name="person_account")
	public String getPersonAccount() {
		return personAccount;
	}

	public void setPersonAccount(String personAccount) {
		this.personAccount = personAccount;
	}

	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name="dt")
	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

}
