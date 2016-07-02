package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhangxb on 2016/6/29.
 * 实名认证-个人
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_realname_private")
public class RealnamePrivate extends IdEntity {
    private static final long serialVersionUID = 1L;

    private Tenant tenant;//所属租户
    private String name;//姓名
    private String idNumber;//'身份证号
    private String idPhoto;//身份证照片
    private String idType;//认证证件类型
    private Date createTime;//创建时间
    private int status;//实名认证状态
    public RealnamePrivate() {
    }

    public RealnamePrivate(String name, String idNumber, String idPhoto, String tenantId, String idType) {
        this.name = name;
        this.idNumber = idNumber;
        this.idPhoto = idPhoto;
        this.idType = idType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "id_type")
    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "id_number")
    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
    @Column(name = "id_photo")
    public String getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(String idPhoto) {
        this.idPhoto = idPhoto;
    }


    @Override
    public String toString() {
        return "RealnamePrivate{" +
                "tenant=" + tenant +
                ", name='" + name + '\'' +
                ", idNumber='" + idNumber + '\'' +
                ", idPhoto='" + idPhoto + '\'' +
                ", idType='" + idType + '\'' +
                ", createTime=" + createTime +
                ", status=" + status +
                '}';
    }
}
