package com.lsxy.framework.api.base;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lsxy.framework.core.utils.JSONUtil;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;


/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * 子类可重载getId()函数重定义id的列名映射和生成策略.
 * 
 * @author sds
 */
//JPA 基类的标识
@MappedSuperclass
public abstract class IdEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
//	private static final long serialVersionUID = 1L;

	protected String id;

	protected boolean deleted = false; //是否删除

	protected Date createTime = new Date(); //创建时间

	protected Date lastTime = new Date();  //最后修改时间

	protected Date deleteTime; //删除时间

	protected Long sortNo = new Date().getTime(); //排序号

	protected int version = 0; //版本号

	//deleted属性不需要进行json序列化
	@JsonIgnore
	@Column(name="deleted")
	public boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "com.lsxy.framework.api.base.CustomUUIDGenerator")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="last_time")
	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@JsonIgnore
	@Column(name="delete_time")
	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@JsonIgnore
	@Column(name="sortno")
	public Long getSortNo() {
		return sortNo;
	}

	public void setSortNo(Long sortNo) {
		this.sortNo = sortNo;
	}

	@JsonIgnore
	@Column(name="version")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString(){
		return JSONUtil.objectToJson(this);
	}
}
