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
    public ApiCertificateSubAccount createSubAccount(ApiCertificateSubAccount subAccount) {
        App app = appService.findById(subAccount.getAppId());
        if(app == null){
            //TODO
            throw new RuntimeException("应用不存在");
        }
        String tenantId = app.getTenant().getId();
        ApiCertificate cert = apiCertificateService.findApiCertificateByTenantId(tenantId);
        subAccount.setTenantId(tenantId);
        subAccount.setType(ApiCertificate.TYPE_SUBACCOUNT);
        subAccount.setCertId(UUIDGenerator.uuid());
        subAccount.setSecretKey(UUIDGenerator.uuid());
        subAccount.setAppId(subAccount.getAppId());
        subAccount.setCallbackUrl(subAccount.getCallbackUrl());
        subAccount.setParentId(cert.getId());
        subAccount.setRemark(subAccount.getRemark());
        this.save(subAccount);
        List<CertAccountQuota> subQuotas = new ArrayList<>();
        if(subAccount.getQuotas() != null){
            for(CertAccountQuota quota : subAccount.getQuotas() ){
                CertAccountQuota saveQ = new CertAccountQuota(tenantId,subAccount.getAppId(),subAccount.getId(),quota.getType(),quota.getValue(),quota.getRemark());
                certAccountQuotaService.save(saveQ);
                subQuotas.add(saveQ);
            }
        }else{

        }
        subAccount.setQuotas(subQuotas);
        return subAccount;
    }

    @Override
    public void deleteSubAccount(String tenantId,String appId,String subAccountId){
        ApiCertificateSubAccount subAccount = this.findById(subAccountId);
        if(subAccount.getTenantId().equals(tenantId) && subAccount.getAppId().equals(appId)){

        }
    }

    @Override
    public ApiCertificateSubAccount updateSubAccount(ApiCertificateSubAccount subAccount) {
        return null;
    }


}
