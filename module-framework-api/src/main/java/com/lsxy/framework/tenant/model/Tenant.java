package com.lsxy.framework.tenant.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;

/**
 * 
 * 租户
 * 
 * @author WangYun
 * 
 */
@Entity
@Table(schema="hsy_service_tenant",name = "tb_tenant")
@XmlRootElement
public class Tenant extends IdEntity{
	private static final long serialVersionUID = 1L;




	// 租户唯一名称
	private String tenantUn;

	// 租户序列号 1-100000
	private int tenantUid;

	// 租户名称
	private String tenantName;

	// 状态 :
	private int tenantStatus;

	// 备注
	private String tenantRemark;

	// 租户类型： 1-公司;2-个体
	private String tenantKind="1";
	
	// 租户类型：1：代理运营商；2：普通租户
	private int tenantType;
	
	// LOGO图片地址
	private String logoUrl;

	// 创建时间
	private Date createDt = new Date();


	public static String[] toJsonProperties = new String[] { "id", "tenantUn",
			"tenantName", "tenantStatus", "tenantRemark", "tenantKind",
			"logoUrl", "createDt", "checkTime", "time", "useWeibo", "useTaobao", "useJd", "useZxPC", "useZxWX", "useZxWeibo"};

	@Column(name = "tenant_uid")
	public int getTenantUid() {
		return tenantUid;
	}

	public void setTenantUid(int tenantUid) {
		this.tenantUid = tenantUid;
	}

	@Column(name = "tenant_un")
	public String getTenantUn() {
		return tenantUn;
	}

	public void setTenantUn(String tenantUn) {
		this.tenantUn = tenantUn;
	}

	@Column(name = "tenant_name")
	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	@Column(name = "tenant_status")
	public int getTenantStatus() {
		return tenantStatus;
	}

	public void setTenantStatus(int tenantStatus) {
		this.tenantStatus = tenantStatus;
	}

	@Column(name = "tenant_remark")
	public String getTenantRemark() {
		return tenantRemark;
	}

	public void setTenantRemark(String tenantRemark) {
		this.tenantRemark = tenantRemark;
	}

	@Column(name = "tenant_kind")
	public String getTenantKind() {
		return tenantKind;
	}

	public void setTenantKind(String tenantKind) {
		this.tenantKind = tenantKind;
	}

	@Column(name = "logo_url")
	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	@Column(name = "createdt")
	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

//
//	@OneToMany(mappedBy = "tenant", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonIgnore
//	public List<TenantContact> getContacts() {
//		return contacts;
//	}
//
//	public void setContacts(List<TenantContact> contacts) {
//		this.contacts = contacts;
//	}

	public String toJson() {
		return JSONUtil.objectToJson(this, toJsonProperties);
	}

//	@Column(name="use_wb")
//	public Integer getUseWeibo() {
//		return useWeibo;
//	}
//
//	public void setUseWeibo(Integer useWeibo) {
//		this.useWeibo = useWeibo;
//	}
//
//	@Column(name="use_taobao")
//	public Integer getUseTaobao() {
//		return useTaobao;
//	}
//
//	public void setUseTaobao(Integer useTaobao) {
//		this.useTaobao = useTaobao;
//	}
//
//	@Column(name="use_jd")
//	public Integer getUseJd() {
//		return useJd;
//	}
//
//	public void setUseJd(Integer useJd) {
//		this.useJd = useJd;
//	}

//	@Column(name="use_wx")
//	public Integer getUseWeixin() {
//		return useWeixin;
//	}
//
//	public void setUseWeixin(Integer useWeixin) {
//		this.useWeixin = useWeixin;
//	}

//	@Column(name="use_zx_pc")
//	public Integer getUseZxPC() {
//		return useZxPC;
//	}
//
//	public void setUseZxPC(Integer useZxPC) {
//		this.useZxPC = useZxPC;
//	}
//
//	@Column(name="use_zx_wx")
//	public Integer getUseZxWX() {
//		return useZxWX;
//	}
//
//	public void setUseZxWX(Integer useZxWX) {
//		this.useZxWX = useZxWX;
//	}
//
//	@Column(name="use_zx_weibo")
//	public Integer getUseZxWeibo() {
//		return useZxWeibo;
//	}
//
//	public void setUseZxWeibo(Integer useZxWeibo) {
//		this.useZxWeibo = useZxWeibo;
//	}

//	@OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonIgnore
//	public Set<TenantZX> getTenantZx() {
//		return tenantZx;
//	}
//
//	public void setTenantZx(Set<TenantZX> tenantZx) {
//		this.tenantZx = tenantZx;
//	}

//	@Column(name="scene_id")
//	public Integer getSceneId() {
//		return sceneId;
//	}
//
//	public void setSceneId(Integer sceneId) {
//		this.sceneId = sceneId;
//	}

	@Column(name="tenant_type")
	public int getTenantType() {
		return tenantType;
	}

	public void setTenantType(int tenantType) {
		this.tenantType = tenantType;
	}



	@Transient @JsonIgnore
	public String getCreateDtFmt(){
		return DateUtils.formatDate(this.getCreateDt(), "yyyy-MM-dd HH:mm:ss");
	}
}
