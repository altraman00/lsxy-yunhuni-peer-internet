package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 全局号码资源
 * Created by zhangxb on 2016/7/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_oc_resource_telenum")
public class ResourceTelenum extends IdEntity{
    public static final int STATUS_LOCK = 2;//临时被锁定
    public static final int STATUS_RENTED = 1; //已被租用
    public static final int STATUS_FREE = 0;    //未被租用
    public static final String USABLE_TRUE = "1";    //可用
    public static final String USABLE_FALSE = "0";    //不可用
    public static final String[] OPERATORS = {"中国电信","中国移动","中国联通"};
    public static final String ISDIALING_TRUE = "1";
    public static final String ISDIALING_FALSE = "0";
    public static final String ISCALLED_TRUE = "1";
    public static final String ISCALLED_FALSE = "0";
    public static final String ISTHROUGH_TRUE = "1";
    public static final String ISTHROUGH_FALSE = "0";

    private Integer status;//1:已被租用 0:未被租用
    private String telNumber;//号码
    private String level;//号码等级,
    private String callUri;//呼出URI,
    private String source;//来源
    private String usable;//是否可用
    private String lineId; //所属线路
    private String operator;//运营商
    private String provider;//供应商
    private String remark;//备注
    private String areaCode;//归属地区号
    private String areaId;//区域
    private BigDecimal amount ;//号码占用费
    private String isDialing;//可主叫
    private String isCalled;//可被叫
    private String isThrough;//可透传
    private String type;//1采购线路0租户自带
    private String appId;
    private String tenantId;

    @Transient
    private LineGateway line;
    @Transient
    private Tenant tenant;//所属租户

    @Column(name = "is_through")
    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "area_id")
    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Column(name = "is_dialing")
    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }
    @Column(name = "is_called")
    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }

    @Column(name = "level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
    @Column(name = "call_uri")
    public String getCallUri() {
        return callUri;
    }

    public void setCallUri(String callUri) {
        this.callUri = callUri;
    }
    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    @Column(name = "usable")
    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }
    @Column(name = "line_id")
    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "area_code")
    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }


    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "tel_number")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public ResourceTelenum(String telNumber, String callUri, String operator, String areaCode, String lineId, String amount) {
        this.operator = operator;
        this.areaCode = areaCode;
        this.amount = new BigDecimal(amount);
        this.callUri = callUri;
        this.telNumber = telNumber;
        this.lineId = lineId;
        this.status =0;//未被租用
        this.usable = "0";//不可用
        this.type = "1";//采购线路
    }
    public ResourceTelenum(String telNumber,String callUri,String operator, String areaCode,String lineId, String amount,String isCalled,String isDialing,String isThrough,String areaId) {
        this.operator = operator;
        this.areaCode = areaCode;
        this.amount = new BigDecimal(amount);
        this.callUri = callUri;
        this.telNumber = telNumber;
        this.lineId = lineId;
        this.status =0;//未被租用
        this.usable = "0";//不可用
        this.type = "1";//采购线路
        this.isCalled = isCalled;
        this.isDialing = isDialing;
        this.isThrough = isThrough;
        this.areaId = areaId;
    }
    public ResourceTelenum() {
    }

    @Transient
    public LineGateway getLine() {
        return line;
    }

    public void setLine(LineGateway line) {
        this.line = line;
    }

    @Transient
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
