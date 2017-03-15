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
    private List<Edit> ids;

    public List<Edit> getIds() {
        return ids;
    }

    public void setIds(List<Edit> ids) {
        this.ids = ids;
    }

    public class Edit{
        @ApiModelProperty(name="id",value = "记录ID")
        private String id;
        @ApiModelProperty(name="tempId",value = "供应商模板ID")
        private String tempId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTempId() {
            return tempId;
        }

        public void setTempId(String tempId) {
            this.tempId = tempId;
        }
    }
}
