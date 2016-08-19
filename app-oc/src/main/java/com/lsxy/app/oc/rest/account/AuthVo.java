package com.lsxy.app.oc.rest.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/8/16.
 */
@ApiModel
public class AuthVo {
    @ApiModelProperty(name = "type",value = "0个人认证 1实名认证 必填")
    private Integer type;//
    @ApiModelProperty(name = "status",value = "1个人成功 2企业成功 -1个人失败 -2企业失败 必填")
    private Integer status;
    @ApiModelProperty(name = "reason",value = "原因 必填")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
