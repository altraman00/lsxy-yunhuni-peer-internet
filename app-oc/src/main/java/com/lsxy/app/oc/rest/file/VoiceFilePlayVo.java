package com.lsxy.app.oc.rest.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;

/**
 * Created by zhangxb on 2016/8/16.
 */
@ApiModel
public class VoiceFilePlayVo {
    @ApiModelProperty(name="reason",value = "原因")
    private String reason;
    @ApiModelProperty(name = "status",value = "通过1 不通过-1 必填")
    private Integer status;


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
