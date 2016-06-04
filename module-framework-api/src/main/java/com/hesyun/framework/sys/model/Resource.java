package com.hesyun.framework.sys.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.lsxy.framework.core.persistence.IdEntity;

/**
 * 系统权限表
 * 
 * @author WangYun
 *
 */
@Entity
@Table(schema="hsy_service_tenant",name="tb_sys_resources")
@Where(clause="deleted=0")
public class Resource extends IdEntity {
	
	//系统级资源
	public static final int TYPE_SYSTEM=1;
	//模块级资源
	public static final int TYPE_MODULE=2;
	//动作级资源
	public static final int TYPE_ACTION=3;
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 资源符号，标识
	//资源类型不同，标识不一样，对于模块类型的资源，为模块的url
	//如果是系统类型，则为系统名称 比如：com.hesyun.app.portal
	//如果是操作类型，则为操作名称  比如 create delete modify
	private String symbol;
	
	// 模块名称
	private String name;
	
	// 说明
	private String remark;
	
	//资源类型
	private int type;
	
	@Column(name="type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	private Resource parent;
	
	@NotFound(action=NotFoundAction.IGNORE)@ManyToOne
	@JoinColumn(name="parent_id")
	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}
	
	@Column(name="symbol")
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Column(name="name")
	public String getName() {
		return name; 
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
