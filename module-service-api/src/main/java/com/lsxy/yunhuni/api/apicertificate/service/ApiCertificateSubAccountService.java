package com.lsxy.yunhuni.api.apicertificate.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;

import java.util.List;

/**
 * Created by liups on 2017/2/14.
 */
public interface ApiCertificateSubAccountService extends BaseService<ApiCertificateSubAccount> {
    ApiCertificateSubAccount createSubAccount(ApiCertificateSubAccount subAccount);

    void deleteSubAccount(String tenantId,String appId,String subAccountId);

    void updateSubAccount(ApiCertificateSubAccount subAccount);

    ApiCertificateSubAccount findByIdWithQuota(String subAccountId);

    Page<ApiCertificateSubAccount> pageListWithQuota(String appId,int pageNo, int pageSize);

    Page<ApiCertificateSubAccount> pageListWithNotQuota(String appId,int pageNo, int pageSize);
}
