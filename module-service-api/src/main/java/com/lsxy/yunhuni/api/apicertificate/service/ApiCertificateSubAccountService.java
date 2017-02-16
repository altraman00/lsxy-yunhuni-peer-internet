package com.lsxy.yunhuni.api.apicertificate.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;

import java.util.List;

/**
 * Created by liups on 2017/2/14.
 */
public interface ApiCertificateSubAccountService extends BaseService<ApiCertificateSubAccount> {
    ApiCertificateSubAccount createSubAccount(String appId, String callbackUrl ,String remark);

    ApiCertificateSubAccount createSubAccount(String appId,String callbackUrl, String remark, List<CertAccountQuota> quotas);
}
