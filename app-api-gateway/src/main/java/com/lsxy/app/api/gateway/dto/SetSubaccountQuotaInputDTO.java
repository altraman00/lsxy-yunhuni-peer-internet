package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 子账号配额
 * Created by liuws on 2016/8/24.
 */
public class SetSubaccountQuotaInputDTO extends CommonDTO{

    @NotNull
    private List<QuotaDTO> quotas;

    public List<QuotaDTO> getQuotas() {
        return quotas;
    }

    public void setQuotas(List<QuotaDTO> quotas) {
        this.quotas = quotas;
    }
}
