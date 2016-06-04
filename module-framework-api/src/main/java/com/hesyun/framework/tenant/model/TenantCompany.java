package com.hesyun.framework.tenant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.core.utils.JSONUtil2Serialization;

/**
 * 租户公司信息
 * 
 * @author WangYun
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_tenant_company")
@Where(clause="deleted=0")
public class TenantCompany extends IdEntity implements JSONUtil2Serialization {
	private static final long serialVersionUID = 1L;
	// 租户
	private Tenant tenant;
	
	// 公司名称
	private String companyName;
	
	// 公司性质
	private String companyKind;
	
	// 公司规模
	private String companyScale;
	
	// 公司地址
	private String companyAddr;

	@OneToOne
	@NotFound(action=NotFoundAction.IGNORE)
	@JoinColumn(name="tenant_id")
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Column(name="company_name")
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name="company_kind")
	public String getCompanyKind() {
		return companyKind;
	}

	public void setCompanyKind(String companyKind) {
		this.companyKind = companyKind;
	}

	@Column(name="company_scale")
	public String getCompanyScale() {
		return companyScale;
	}

	public void setCompanyScale(String companyScale) {
		this.companyScale = companyScale;
	}

	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
