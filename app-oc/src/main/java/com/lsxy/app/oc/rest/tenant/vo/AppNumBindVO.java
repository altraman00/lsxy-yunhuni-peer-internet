package com.lsxy.app.oc.rest.tenant.vo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by liups on 2017/1/11.
 */
public class AppNumBindVO implements Serializable {
    @ApiModelProperty(name = "nums",value = "要绑定的号码")
    String[] nums;

    public String[] getNums() {
        return nums;
    }

    public void setNums(String[] nums) {
        this.nums = nums;
    }
}
