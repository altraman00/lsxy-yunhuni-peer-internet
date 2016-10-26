package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/10/25.
 */
@ApiModel
public class LineGatewayEditStatusVo {
    @ApiModelProperty(name="status",value = "是否启用：1=是，0=不是")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
