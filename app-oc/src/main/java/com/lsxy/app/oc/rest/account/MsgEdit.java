package com.lsxy.app.oc.rest.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2017/3/15.
 */
@ApiModel
public class MsgEdit {
    @ApiModelProperty(name="id",value = "记录ID")
    private String id;
    @ApiModelProperty(name="tempId",value = "供应商模板ID")
    private String tempId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }
}
