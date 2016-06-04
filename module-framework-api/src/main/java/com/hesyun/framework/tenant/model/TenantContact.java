package com.hesyun.framework.tenant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.core.utils.JSONUtil2Serialization;

/**
 * 租户联系人
 * 
 * @author WangYun
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_tenant_contact")
@Where(clause="deleted=0")
public class TenantContact extends IdEntity  implements JSONUtil2Serialization{
	private static final long serialVersionUID = 1L;
	// 租户
	private Tenant tenant;
	
	// 联系人姓名
	private String contactName;
	
	// 联系人电话1
	private String mobile;
	
	// 联系人电话2
	private String phone;
	
	// 地址
	private String addr;
	
	// 备注
	private String contactRemark;
	
	// 身份证
	private String idnumber;
	
	// 省
	private String province;
	
	// 市
	private String city;
	
	// 区
	private String area;
	
	// 邮件
	private String email;

	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="tenant_id")
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Column(name="mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name="phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="addr")
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	@Column(name="idnumber")
	public String getIdnumber() {
		return idnumber;
	}

	public void setIdnumber(String idnumber) {
		this.idnumber = idnumber;
	}

	@Column(name="province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name="city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name="area")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name="email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name="name")
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name="remark")
	public String getContactRemark() {
		return contactRemark;
	}

	public void setContactRemark(String contactRemark) {
		this.contactRemark = contactRemark;
	}

	@Override
	public String toJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
