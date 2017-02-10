package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.call.center.api.model.AppExtension;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by liups on 2016/11/18.
 */
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

    public ExtensionVO() {
    }

    public ExtensionVO(String id, String type, String user, String password, String ipaddr, String telnum,Boolean enable) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.password = password;
        this.ipaddr = ipaddr;
        this.telnum = telnum;
        this.enable = enable;
    }

    public static ExtensionVO changeAppExtensionToExtensionVO(AppExtension ext){
        return new ExtensionVO(ext.getId(),ext.getType(),ext.getUser(),ext.getPassword(),ext.getIpaddr(),ext.getTelnum(),ext.getEnable());
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
}
