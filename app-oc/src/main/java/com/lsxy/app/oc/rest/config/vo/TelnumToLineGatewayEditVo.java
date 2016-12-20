package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/10/26.
 */
@ApiModel
public class TelnumToLineGatewayEditVo {
    @ApiModelProperty(name="id",value = "标志")
    private String id;
    @ApiModelProperty(name="isDialing",value = "可主叫")
    private String isDialing;
    @ApiModelProperty(name="isCalled",value = "可被叫")
    private String isCalled;
    @ApiModelProperty(name="isThrough",value = "可透传")
    private String isThrough;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsDialing() {
        return isDialing;
    }

    public void setIsDialing(String isDialing) {
        this.isDialing = isDialing;
    }

    public String getIsCalled() {
        return isCalled;
    }

    public void setIsCalled(String isCalled) {
        this.isCalled = isCalled;
    }

    public String getIsThrough() {
        return isThrough;
    }

    public void setIsThrough(String isThrough) {
        this.isThrough = isThrough;
    }
}
