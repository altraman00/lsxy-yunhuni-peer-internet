package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 新增子账号
 * Created by liuws on 2016/8/24.
 */
public class AddSubaccountInputDTO extends CommonDTO{

    @NotNull
    private String callbackUrl;

    @Size(max = 128)
    private String remark;

    private List<QuotaDTO> quotas;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<QuotaDTO> getQuotas() {
        return quotas;
    }

    public void setQuotas(List<QuotaDTO> quotas) {
        this.quotas = quotas;
    }

}
