package com.hesyun.framework.state.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;

/**
 * 登录日志
 * @author lijing
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_sua_log_login")
@Where(clause="deleted=0")
public class LoginLog extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String personId;//用户Id

	private String loginName;//用户登录名
	
	private String personName;//用户姓名

	private Date dt;   //登录时间
	
	private String remark; //备注
	
	private String ip; //登录IP
	
	private String macAddress;//MAC地址
	
	@Column(name="person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}


	@Column(name="login_name")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Column(name="person_name")
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Column(name="dt")
	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name="mac_address")
	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
}
