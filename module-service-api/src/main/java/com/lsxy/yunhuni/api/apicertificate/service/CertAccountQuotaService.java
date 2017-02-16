package com.lsxy.yunhuni.api.apicertificate.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;

import java.util.List;

/**
 * Created by liups on 2017/2/15.
 */
public interface CertAccountQuotaService extends BaseService<CertAccountQuota> {
    void updateQuotas(String appId,List<CertAccountQuota> quotas);
}
