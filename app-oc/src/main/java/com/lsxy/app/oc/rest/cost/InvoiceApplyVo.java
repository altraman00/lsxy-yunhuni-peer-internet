package com.lsxy.app.oc.rest.cost;

import io.swagger.annotations.ApiParam;

/**
 * Created by zhangxb on 2016/8/16.
 */
public class InvoiceApplyVo {
    private String id;
    @ApiParam(name = "status",value = "修改审核时使用状态 1通过2异常")
    private Integer status;
    @ApiParam(name = "expressNo",value = "发送快递时使用:快递单号必填")
    private String expressNo;
    @ApiParam(name = "expressNo",value = "发送快递时使用:快递公司")
    private String expressCom;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
