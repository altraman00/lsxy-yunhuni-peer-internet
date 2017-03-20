package com.lsxy.app.oc.rest.account;

import com.lsxy.app.oc.rest.tenant.vo.TenantAppVO;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.msg.api.model.MsgTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * Created by zhangxb on 2017/3/17.
 */
public class MsgTemplateVo {
    public static final Logger logger = LoggerFactory.getLogger(MsgTemplateVo.class);
    private String id;
    private boolean deleted = false; //是否删除
    private Date createTime = new Date(); //创建时间
    private Date lastTime = new Date();  //最后修改时间
    private String tempId;
    private String tenantId;
    private String appId;
    private String tenantName;
    private String appName;
    private String subaccountId;
    private String name;
    private String type;
    private String content;
    private Integer status;
    private String reason;
    private String remark;
    private String lastUserName;
    private String msgSupplierId;
    private String msgSupplierName;
    public MsgTemplateVo(MsgTemplate msgTemplate,String tenantName,String appName){
        try {
            BeanUtils.copyProperties(this,msgTemplate);
            this.appName = appName;
            this.tenantName = tenantName;
        } catch (IllegalAccessException e) {
            logger.error("复制类属性异常",e);
        } catch (InvocationTargetException e) {
            logger.error("复制类属性异常",e);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

    public String getMsgSupplierId() {
        return msgSupplierId;
    }

    public void setMsgSupplierId(String msgSupplierId) {
        this.msgSupplierId = msgSupplierId;
    }

    public String getMsgSupplierName() {
        return msgSupplierName;
    }

    public void setMsgSupplierName(String msgSupplierName) {
        this.msgSupplierName = msgSupplierName;
    }
}
