package com.hesyun.framework.state.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;

/**
 * 系统操作日志
 * @author lijing
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_sua_log_operation")
@Where(clause="deleted=0")
public class OperateLog extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String operatePerson;//操作人

	private Date operateTime;//操作时间

	private String operateName;//操作名称

	private String remark;//备注
	
	@Column(name="operator")
	public String getOperatePerson() {
		return operatePerson;
	}

	public void setOperatePerson(String operatePerson) {
		this.operatePerson = operatePerson;
	}

	@Column(name="dt")
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@Column(name="op_name")
	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
