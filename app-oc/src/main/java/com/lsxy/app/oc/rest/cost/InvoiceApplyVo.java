package com.lsxy.app.oc.rest.cost;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhangxb on 2016/8/16.
 */
@ApiModel
public class InvoiceApplyVo {
    @ApiModelProperty(name = "status",value = "修改审核时使用状态 1通过2异常")
    private Integer status;
    @ApiModelProperty(name = "expressNo",value = "发送快递时使用:快递单号必填")
    private String expressNo;
    @ApiModelProperty(name = "expressNo",value = "发送快递时使用:快递公司")
    private String expressCom;
    @ApiModelProperty(name = "reason",value = "原因")
    private String reason;

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressCom() {
        return expressCom;
    }

    public void setExpressCom(String expressCom) {
        this.expressCom = expressCom;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
