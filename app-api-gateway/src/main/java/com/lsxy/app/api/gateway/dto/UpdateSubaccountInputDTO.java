package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 修改子账号
 * Created by liuws on 2016/8/24.
 */
public class UpdateSubaccountInputDTO extends CommonDTO{

    @NotNull
    private String callbackUrl;

    @Min(0)
    @Max(1)
    private Integer enabled;

    @Size(max = 128)
    private String remark;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
