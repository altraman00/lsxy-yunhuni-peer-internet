package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.Size;

/**
 * 创建会议
 * Created by liuws on 2016/8/24.
 */
public class ChannelCreateInputDTO extends CommonDTO{

    @JsonProperty("remark")
    @Size(max = 128)
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
