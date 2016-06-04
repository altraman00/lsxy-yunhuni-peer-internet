package com.hesyun.framework.tenant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.core.utils.JSONUtil2;

/**
 * 租户用户
 * @author tandy
 */
@Entity
@Table(schema = "hsy_service_tenant", name = "tb_tenant_account")
@Where(clause = "deleted=0")
public class Account extends IdEntity {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	

	private String name;                    // 员工昵称
	private String number;                //员工编码
	private String email;                    //电子邮件
	private String displayName;        //客服显示名（“客服"+“工号"）
	private String password;            //密码
	private String sex;                    //性别
	private String mobile;                //移动手机
	private String phone;                //座机
	private boolean isAdmin;            //是否租户管理员
	private int status;                    //账号状态			租户用户状态：0-待审核 1-LOCK 2-正常  3-异常
	private Date createDt = new Date();
	private String creator;
	private long sortNo;            //排序号
	private String userName;            //用户名
	private Tenant tenant;                //所属租户
	private String tenantUn;        // 租户标识
	private String headImgUrl;            //头像url
	private String mm;
	private boolean isActived; // 是否激活（客服在企业号中使用，创建时为未激活，客服在企业号中回复后变为已激活状态）

//	private String wxHsyOpenid;			//租户用户关注的和声云的openid,客服没有该id,租户管理员有该id,和声云管理员有该id
//	private String wxUnionId;	//用户对应和声云unionid
//	private String fixrole;	//角色			//固有角色
//	private Set<Role> roles;	//角色

	public static int PERSON_STATUS_WAITFORCHECK = 0;    //待审核
	public static int PERSON_STATUS_LOCKED = 1;            //禁用
	public static int PERSON_STATUS_OK = 2;                //正常
	public static int PERSON_STATUS_NOTOK = 3;            //异常  审核失败
	
	public static boolean PERSON_IS_ACTIVED_YES = true; // 用户已激活
	public static boolean PERSON_IS_ACTIVED_NO = false; // 用户未激活


	public static String[] toJsonProperties =
		new String[]{"id", "number", "isActived", "email", "displayName", "sex", "mobile", "phone", "isAdmin", "status", "mm", "createDt", "creator",
			"sortNo", "userName", "headImgUrl", "name", "tenant", "tenantUn", "tenantUid", "tenantName",};

	@Column(name = "mm")
	@JsonIgnore
	public String getMm() {
		return mm;
	}

	public void setMm(String mm) {
		this.mm = mm;
	}

//	@Column(name="wx_hsy_openid")
//	public String getWxHsyOpenid() {
//		return wxHsyOpenid;
//	}
//
//	public void setWxHsyOpenid(String wxHsyOpenid) {
//		this.wxHsyOpenid = wxHsyOpenid;
//	}

	
	@Column(name = "headimgurl")
	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	@Column(name = "username")
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "number")
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "display_name")
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "password")
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "sex")
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Column(name = "mobile")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "phone")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "is_tenant_admin")
	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Column(name = "status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "createdt")
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	@Column(name = "creator")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "sortno")
	public long getSortNo() {
		return sortNo;
	}

	public void setSortNo(long sortNo) {
		this.sortNo = sortNo;
	}
	

	@Transient
	public boolean validatePassword(String password2) {
		return false;
	}

	@Transient
	@JsonIgnore
	public String getPlaintextPassword() {
		return this.password;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "tenant_id")
	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String toJson() {
		return JSONUtil2.objectToJsonInclude(this, toJsonProperties);
	}

	@Column(name = "is_actived")
	public boolean isActived() {
		return isActived;
	}

	public void setActived(boolean isActived) {
		this.isActived = isActived;
	}


	@Column(name = "tenantun")
	public String getTenantUn() {
		return tenantUn;
	}

	public void setTenantUn(String tenantUn) {
		this.tenantUn = tenantUn;
	}

//	@Column(name="wx_hsy_unionid")
//	public String getWxUnionId() {
//		return wxUnionId;
//	}
//	public void setWxUnionId(String wxUnionId) {
//		this.wxUnionId = wxUnionId;
//	}
	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
