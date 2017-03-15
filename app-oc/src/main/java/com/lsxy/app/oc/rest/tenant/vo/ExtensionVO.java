package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by liups on 2016/11/18.
 */
@ApiModel
public class ExtensionVO {
    @ApiModelProperty(name="id",value = "id")
    private String id;
    @ApiModelProperty(name="type",value = "类型：1:SIP 终端;2:SIP 网关;3:普通电话")
    private String type;//    type                 varchar(30),
    @ApiModelProperty(name="user",value = "用户名")
    private String user;//    user                 varchar(50) comment 'SIP注册用户名，全局唯一。格式是6到12位数字',
    @ApiModelProperty(name="password",value = "密码")
    private String password;//    password             varchar(50) comment 'SIP注册密码',
    @ApiModelProperty(name="ipaddr",value = "sip网关IP地址与端口")
    private String ipaddr;  //SIP 网关IP地址与端口，默认5060，仅用于 type==2的情况
    @ApiModelProperty(name="telnum",value = "电话号码")
    private String telnum;//    telenum              varchar(32) comment '如果是电话分机，该属性记录电话号码（保留，不用）',
    @ApiModelProperty(name="enable",value = "是否可用")
    private Boolean enable; //分机是否可用（不存到数据库）;
    @ApiModelProperty(name = "subaccountId",value = "关联子账号")
    private String subaccountId;
    @ApiModelProperty(name = "certId",value = "关联子账号-鉴权账户")
    private String certId;
    public ExtensionVO() {
    }

    public ExtensionVO(String id, String type, String user, String password, String ipaddr, String telnum,Boolean enable,String subaccountId,String certId) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.password = password;
        this.ipaddr = ipaddr;
        this.telnum = telnum;
        this.enable = enable;
        this.subaccountId = subaccountId;
        this.certId = certId;
    }

    public static ExtensionVO changeAppExtensionToExtensionVO(AppExtension ext, ApiCertificateSubAccount apiCertificateSubAccount){
        String certId = apiCertificateSubAccount!=null ? apiCertificateSubAccount.getCertId() :"";
        return new ExtensionVO(ext.getId(),ext.getType(),ext.getUser(),ext.getPassword(),ext.getIpaddr(),ext.getTelnum(),ext.getEnable(),ext.getSubaccountId(),certId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIpaddr() {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }
}
