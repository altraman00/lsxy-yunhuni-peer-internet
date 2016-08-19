package com.lsxy.app.oc.rest.file;

import io.swagger.annotations.ApiParam;

/**
 * Created by zhangxb on 2016/8/16.
 */
public class VoiceFilePlayVo {
    private String id;
    private String reason;
    @ApiParam(name = "status",value = "通过1 不通过-1 必填")
    private Integer status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
