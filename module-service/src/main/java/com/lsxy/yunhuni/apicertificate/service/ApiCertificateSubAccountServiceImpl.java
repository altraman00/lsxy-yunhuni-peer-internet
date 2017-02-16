package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateSubAccountDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

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
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    ResourceTelenumService resourceTelenumService;

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
        List<CertAccountQuota> quotas = subAccount.getQuotas();
        Collection<CertAccountQuota> setQuotas = getQuotasByApp(app,quotas);
        if(setQuotas != null){
            for(CertAccountQuota quota : setQuotas){
                CertAccountQuota saveQ = new CertAccountQuota(tenantId,subAccount.getAppId(),subAccount.getId(),quota.getType(),quota.getValue(),quota.getRemark());
                certAccountQuotaService.save(saveQ);
                subQuotas.add(saveQ);
            }
        }
        subAccount.setQuotas(subQuotas);
        return subAccount;
    }

    private Collection<CertAccountQuota> getQuotasByApp(App app, List<CertAccountQuota> quotas) {
        Map<String,CertAccountQuota> map = new HashMap();
        for(CertAccountQuota quota : quotas){
            if(StringUtils.isNotBlank(quota.getType()) && map.get(quota.getType()) == null){
                map.put(quota.getType(),quota);
            }
        }
        Set<String> keys = map.keySet();
        if(!keys.contains(CertAccountQuotaType.CallQuota.name())){
            map.put(CertAccountQuotaType.CallQuota.name(),new CertAccountQuota(CertAccountQuotaType.CallQuota.name()));
        }
        if(app.getType().equals(App.PRODUCT_CALL_CENTER)){
            if(!keys.contains(CertAccountQuotaType.AgentQuota.name())){
                map.put(CertAccountQuotaType.AgentQuota.name(),new CertAccountQuota(CertAccountQuotaType.AgentQuota.name()));
            }
        }
        return map.values();
    }

    @Override
    public void deleteSubAccount(String tenantId,String appId,String subAccountId){
        ApiCertificateSubAccount subAccount = this.findById(subAccountId);
        if(subAccount.getTenantId().equals(tenantId) && subAccount.getAppId().equals(appId)){
            resourceTelenumService.subaccountUnbindAll(tenantId,appId,subAccountId);
        }
    }

    @Override
    public void updateSubAccount(ApiCertificateSubAccount subAccount) {
        ApiCertificateSubAccount saveAccount = this.findById(subAccount.getId());
        if(saveAccount.getAppId().equals(subAccount.getAppId())){
            saveAccount.setCallbackUrl(subAccount.getCallbackUrl());
            saveAccount.setEnabled(subAccount.getEnabled());
            saveAccount.setRemark(saveAccount.getRemark());
            this.save(saveAccount);
        }
//        certAccountQuotaService.updateQuotas();
    }

    @Override
    public Page<ApiCertificateSubAccount> pageListWithQuota(String appId, int pageNo, int pageSize) {
        return null;
    }

    @Override
    public Page<ApiCertificateSubAccount> pageListWithNotQuota(String appId, int pageNo, int pageSize) {
        return null;
    }


}
