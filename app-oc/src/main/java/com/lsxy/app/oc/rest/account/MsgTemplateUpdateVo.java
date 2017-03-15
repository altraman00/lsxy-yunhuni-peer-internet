package com.lsxy.app.oc.rest.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import sun.applet.Main;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/26.
 */
@ApiModel
public class MsgTemplateUpdateVo {
    @ApiModelProperty(name="ids",value = "修改集合")
    private List<MsgEdit> ids;

    public List<MsgEdit> getIds() {
        return ids;
    }

    public void setIds(List<MsgEdit> ids) {
        this.ids = ids;
    }
}
