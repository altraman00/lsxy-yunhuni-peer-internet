package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/10/26.
 */
@ApiModel
public class TelnumToLineGatewayBatchEditVo {
    @ApiModelProperty(name="telnums",value = "修改配置号码集合")
    TelnumToLineGatewayEditVo[] telnums;

    public TelnumToLineGatewayEditVo[] getTelnums() {
        return telnums;
    }

    public void setTelnums(TelnumToLineGatewayEditVo[] telnums) {
        this.telnums = telnums;
    }
}
