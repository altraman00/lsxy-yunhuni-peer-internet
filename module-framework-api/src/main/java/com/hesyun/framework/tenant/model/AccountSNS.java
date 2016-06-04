package com.hesyun.framework.tenant.model;

import java.io.Serializable;
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
import com.lsxy.framework.core.utils.JSONUtil2Serialization;
/**
 * 租户用户
 * @author tandy
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name = "tb_tenant_account_sns")
@Where(clause="deleted=0")
public class AccountSNS extends IdEntity implements Serializable,JSONUtil2Serialization{

	private Account account;
	private String openid;
	private String type;
	private Date bindDt;
	
	public static final String TYPE_WEIXIN="Weixin";
	public static final String TYPE_SINA_WEIBO="SinaWeibo";
	public static final String TYPE_QQ="QQ";
	

	
	@NotFound(action=NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name="account_id")
	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	@Column(name="openid")
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Column(name="type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name="bind_dt")
	public Date getBindDt() {
		return bindDt;
	}

	public void setBindDt(Date bindDt) {
		this.bindDt = bindDt;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String toJson() {
		
		return null;
	}
	

}
