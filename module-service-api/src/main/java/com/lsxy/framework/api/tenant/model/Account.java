package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.api.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 租户用户
 * @author tandy
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_base", name = "tb_base_account")
public class Account extends IdEntity {

	public static final int STATUS_NOT_ACTIVE = 0; 	//账号未激活
	public static final int STATUS_LOCK = 1; 		//账号锁定
	public static final int STATUS_NORMAL = 2; 		//账号正常
	public static final int STATUS_ABNORMAL = 3; 	//账号异常
	public static final int STATUS_EXPIRE = 4; 		//账号过期

	private String userName;
	private Tenant tenant;                //所属租户
	private String mobile;                //移动手机
	private String email;                    //电子邮件
	private String password;            //密码
	private String mm;

	private Integer status;                    //账号状态			租户用户状态：0-未激活 1-LOCK 2-正常  3-异常

	private String address;//地址

	private String phone;//非绑定电话

	private String industry;//应用行业

	private String business;//主要业务

	private String url;//网站

	private String province;//省份

	private String city;//城市

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
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}



	@ManyToOne
	@JoinColumn(name = "tenant_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name = "address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "industry")
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
	@Column(name = "business")
	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}
	@Column(name = "url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name = "province")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name = "city")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		String account =  "Account{" +
				"userName='" + userName + '\'' +
				", mobile='" + mobile + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", mm='" + mm + '\'' +
				", status=" + status +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", industry='" + industry + '\'' +
				", business='" + business + '\'' +
				", url='" + url + '\'' +
				", province='" + province + '\'' +
				", city='" + city + '\'' ;
		if(tenant!=null){
			account+=",tenatn='"+tenant.toString()+"'";
		}
		account+='}';
		return account;
	}
}
