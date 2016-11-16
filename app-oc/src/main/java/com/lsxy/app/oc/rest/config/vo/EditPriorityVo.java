package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/10/25.
 */
@ApiModel
public class EditPriorityVo {
    @ApiModelProperty(name="priority",value = "目标有限级")
    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
