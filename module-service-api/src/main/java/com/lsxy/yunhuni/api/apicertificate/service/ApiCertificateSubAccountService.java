package com.lsxy.yunhuni.api.apicertificate.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by liups on 2017/2/14.
 */
public interface ApiCertificateSubAccountService extends BaseService<ApiCertificateSubAccount> {
    ApiCertificateSubAccount createSubAccount(ApiCertificateSubAccount subAccount);

    void deleteSubAccount(String tenantId,String appId,String subAccountId) throws InvocationTargetException, IllegalAccessException;

    void updateSubAccount(ApiCertificateSubAccount subAccount);

    ApiCertificateSubAccount findByIdWithQuota(String subAccountId);

    Page<ApiCertificateSubAccount> pageListWithQuota(String appId,int pageNo, int pageSize);

    Page<ApiCertificateSubAccount> pageListWithNotQuota(String appId,int pageNo, int pageSize);

    Page<ApiCertificateSubAccount> pageListWithQuotaByCondition(String appId,int pageNo, int pageSize,String certId,String remark,Integer  enabled);
    List<ApiCertificateSubAccount> findByAppId(String appId);
    /**
     * 判断子账号是否一致
     * @param sub1
     * @param sub2
     * @return
     */
    boolean subaccountCheck(String sub1,String sub2);

}
