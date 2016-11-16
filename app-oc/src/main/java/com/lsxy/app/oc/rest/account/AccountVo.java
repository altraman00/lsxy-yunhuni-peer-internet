package com.lsxy.app.oc.rest.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by zhangxb on 2016/10/19.
 */
@ApiModel
public class AccountVo {
    @ApiModelProperty(name = "id",value = "用户标志")
    private String id;//用户标志
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private Date createTime;//创建时间
    @ApiModelProperty(name = "userName",value = "用户名")
    private String userName;//用户名
    @ApiModelProperty(name = "mobile",value = "移动手机")
    private String mobile;//移动手机
    @ApiModelProperty(name = "email",value = "电子邮件")
    private String email;//电子邮件

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
