package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/10/27.
 */
@ApiModel
public class ApiGwRedBlankNumVo {
    @ApiModelProperty(name="number",value = "号码")
    private String number;
    @ApiModelProperty(name="type",value = "1红名单，2黑名单")
    private Integer type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
