package com.lsxy.app.oc.rest.account;

import io.swagger.annotations.ApiParam;

/**
 * Created by zhangxb on 2016/8/16.
 */
public class AuthVo {
    @ApiParam(name = "type",value = "0个人认证 1实名认证 必填")
    private String uid;
    @ApiParam(name = "type",value = "0个人认证 1实名认证 必填")
    private Integer type;//
    @ApiParam(name = "status",value = "1个人成功 2企业成功 -1个人失败 -2企业失败 必填")
    private Integer status;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
