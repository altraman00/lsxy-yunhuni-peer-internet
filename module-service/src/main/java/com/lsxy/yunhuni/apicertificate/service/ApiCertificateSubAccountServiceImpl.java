package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateSubAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2017/2/14.
 */
@Service
public class ApiCertificateSubAccountServiceImpl extends AbstractService<ApiCertificateSubAccount> implements ApiCertificateSubAccountService {
    @Autowired
    private ApiCertificateSubAccountDao apiCertificateSubAccountDao;
    @Autowired
    private AppService appService;
    @Autowired
    private ApiCertificateService apiCertificateService;
    @Autowired
    private CertAccountQuotaService certAccountQuotaService;


    @Override
    public BaseDaoInterface<ApiCertificateSubAccount, Serializable> getDao() {
        return this.apiCertificateSubAccountDao;
    }

    @Override
    public ApiCertificateSubAccount createSubAccount(String appId, String callbackUrl, List<CertAccountQuota> quotas) {
        App app = appService.findById(appId);
        if(app == null){
            //TODO
            throw new RuntimeException("应用不存在");
        }
        String tenantId = app.getTenant().getId();
        ApiCertificate cert = apiCertificateService.findApiCertificateByTenantId(tenantId);
        ApiCertificateSubAccount subAccount = new ApiCertificateSubAccount();
        subAccount.setTenantId(tenantId);
        subAccount.setType(ApiCertificate.TYPE_SUBACCOUNT);
        subAccount.setCertId(UUIDGenerator.uuid());
        subAccount.setSecretKey(UUIDGenerator.uuid());
        subAccount.setAppId(appId);
        subAccount.setCallbackUrl(callbackUrl);
        subAccount.setParentId(cert.getId());
        this.save(subAccount);
        List<CertAccountQuota> subQuotas = new ArrayList<>();
        for(CertAccountQuota quota : quotas){
            CertAccountQuota saveQ = new CertAccountQuota(tenantId,appId,subAccount.getId(),quota.getType(),quota.getSum(),quota.getRemark());
            certAccountQuotaService.save(saveQ);
            subQuotas.add(saveQ);
        }
        subAccount.setQuotas(subQuotas);
        return subAccount;
    }



}
