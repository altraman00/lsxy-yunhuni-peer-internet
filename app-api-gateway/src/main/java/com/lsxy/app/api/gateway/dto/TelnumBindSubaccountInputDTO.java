package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 号码绑定子账号
 * Created by liuws on 2016/8/24.
 */
public class TelnumBindSubaccountInputDTO extends CommonDTO{

    @NotNull
    private String subaccountId;

    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }
}
