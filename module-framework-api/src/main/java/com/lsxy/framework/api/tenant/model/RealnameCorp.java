package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.core.persistence.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by zhangxb on 2016/6/29.
 * 实名认证-企业
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_realname_corp")
public class RealnameCorp extends IdEntity {
    private static final long serialVersionUID = 1L;
    private Tenant tenant;//所属租户
    private String  name;// 企业名称
    private String  addr;// 企业地址
    private String  fieldCode;// 所属行业代号,数据字典
    private String  contacts1;// 联系人1
    private String  contacts2;// 联系人2
    private String  authType;// 认证类型
    private String  type01Prop01;// [一照一码]营业执照照片
    private String  type01Prop02;//type01_prop02 [一照一码]统一社会信用代码
    private String  type02Prop01;//type02_prop01 [三证合一]注册号
    private String  type02Prop02;//type02_prop02 [三证合一]税务登记证号
    private String  type02Prop03;//type02_prop03 [三证合一]营业执照照片
    private String  type03Prop01;//type03_prop01 [三证分离]税务登记号
    private String  type03Prop02;//type03_prop02 [三证分离]税务登记证照片
    private String  type03Prop03;//type03_prop03 [三证分离]营业执照号
    private String  type03Prop04;//type03_prop04 [三证分离]营业执照照片
    private int status;//实名认证状态
    private Date createTime;//创建时间
    public RealnameCorp() {
    }

    public RealnameCorp(String tenantId,String name,String addr,String fieldCode,String authType,String type01Prop01,String type01Prop02,String type02Prop01,String type02Prop02,String type03Prop02 ){
        this.name = name;
        this.addr = addr;
        this.fieldCode = fieldCode;
        this.authType = authType;
        this.type01Prop01 = type01Prop01;
        this.type01Prop02 = type01Prop02;
        this.type02Prop01 = type02Prop01;
        this.type02Prop02 = type02Prop02;
        this.type03Prop02 = type03Prop02;
    }
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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


    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "addr")
    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
    @Column(name = "field_code")
    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }
    @Column(name = "contacts1")
    public String getContacts1() {
        return contacts1;
    }

    public void setContacts1(String contacts1) {
        this.contacts1 = contacts1;
    }
    @Column(name = "contacts2")
    public String getContacts2() {
        return contacts2;
    }

    public void setContacts2(String contacts2) {
        this.contacts2 = contacts2;
    }
    @Column(name = "auth_type")
    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
    @Column(name = "type01_prop01")
    public String getType01Prop01() {
        return type01Prop01;
    }

    public void setType01Prop01(String type01Prop01) {
        this.type01Prop01 = type01Prop01;
    }
    @Column(name = "type01_prop02")
    public String getType01Prop02() {
        return type01Prop02;
    }

    public void setType01Prop02(String type01Prop02) {
        this.type01Prop02 = type01Prop02;
    }
    @Column(name = "type02_prop01")
    public String getType02Prop01() {
        return type02Prop01;
    }

    public void setType02Prop01(String type02Prop01) {
        this.type02Prop01 = type02Prop01;
    }
    @Column(name = "type02_prop02")
    public String getType02Prop02() {
        return type02Prop02;
    }

    public void setType02Prop02(String type02Prop02) {
        this.type02Prop02 = type02Prop02;
    }
    @Column(name = "type02_prop03")
    public String getType02Prop03() {
        return type02Prop03;
    }

    public void setType02Prop03(String type02Prop03) {
        this.type02Prop03 = type02Prop03;
    }
    @Column(name = "type03_prop01")
    public String getType03Prop01() {
        return type03Prop01;
    }

    public void setType03Prop01(String type03Prop01) {
        this.type03Prop01 = type03Prop01;
    }
    @Column(name = "type03_prop02")
    public String getType03Prop02() {
        return type03Prop02;
    }

    public void setType03Prop02(String type03Prop02) {
        this.type03Prop02 = type03Prop02;
    }
    @Column(name = "type03_prop03")
    public String getType03Prop03() {
        return type03Prop03;
    }

    public void setType03Prop03(String type03Prop03) {
        this.type03Prop03 = type03Prop03;
    }
    @Column(name = "type03_prop04")
    public String getType03Prop04() {
        return type03Prop04;
    }

    public void setType03Prop04(String type03Prop04) {
        this.type03Prop04 = type03Prop04;
    }
}
