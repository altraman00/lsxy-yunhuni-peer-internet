package com.lsxy.app.oc.rest.config.vo;

import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelnumToLineGateway;

/**
 * Created by zhangxb on 2016/11/10.
 */
public class TelnumToLineGatewayVo extends TelnumToLineGateway {

    ResourceTelenum resourceTelenum;

    public ResourceTelenum getResourceTelenum() {
        return resourceTelenum;
    }

    public void setResourceTelenum(ResourceTelenum resourceTelenum) {
        this.resourceTelenum = resourceTelenum;
    }
}
