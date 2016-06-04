package com.hesyun.framework.state.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;
import com.hesyun.framework.tenant.model.Account;
import com.hesyun.framework.tenant.model.Tenant;

/**
 * 系统动态
 * 
 * @author sds
 * 
 */
@Entity
@Table(schema="hsy_service_tenant",name = "tb_tenant_xtdt")
@Where(clause="deleted=0")
public class Xtdt extends IdEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;       //标题
	private Date dt;            //时间
	private Account person;      //用户ID
	private Tenant tenant;	    //所属租户
	private String tenantUn;  //租户名称
	private int    operateType; //操作类型
	private String ip;          //ip地址
	private String detail;      //动态详情
	

	@Column(name="caption")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="dt")
	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="user_id")
	public Account getPerson() {
		return person;
	}

	public void setPerson(Account person) {
		this.person = person;
	}
	
	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="tenant_id")
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Column(name="tenantun")
	public String getTenantUn() {
		return tenantUn;
	}

	public void setTenantUn(String tenantUn) {
		this.tenantUn = tenantUn;
	}
	

	@Column(name="operate_type")
	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	@Column(name="ip")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name="detail")
	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
