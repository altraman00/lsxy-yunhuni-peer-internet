package com.lsxy.app.oc.rest.custservice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/8/19.
 */
@ApiModel
public class FeedbackVo {
    @ApiModelProperty(name = "ids",value = "信息的id数组")
    private String[] ids;

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
