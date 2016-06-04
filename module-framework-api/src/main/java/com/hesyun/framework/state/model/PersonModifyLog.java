package com.hesyun.framework.state.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;

/**
 * 密码修改日志
 * @author lijing
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_sua_person_modi_his")
@Where(clause="deleted=0")
public class PersonModifyLog extends IdEntity{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String account;			//用户账号

	private int operateType;		//操作类型

	private Date dt;				//操作时间	

	private String personId;		//操作者ID
	
	private String personName;		//操作者姓名
	
	private String content;			//操作内容
	
	private int  synStatus;			//同步状态

	private String taskInsId;       //对应任务实例Id
	
	
	@Column(name="account")
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Column(name="op_type")
	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	@Column(name="dt")
	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	@Column(name="person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Column(name="syn_status")
	public int getSynStatus() {
		return synStatus;
	}

	public void setSynStatus(int synStatus) {
		this.synStatus = synStatus;
	}

	@Column(name="taskins_id")
	public String getTaskInsId() {
		return taskInsId;
	}

	public void setTaskInsId(String taskInsId) {
		this.taskInsId = taskInsId;
	}

	@Column(name="person_name")
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	@Column(name="content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

//	@Column(name="syn_status")
//	public int getSynStatus() {
//		return synStatus;
//	}
//
//	public void setSynStatus(int synStatus) {
//		this.synStatus = synStatus;
//	}

}
