package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.tenant.model.Tenant;
import com.lsxy.framework.tenant.service.AccountService;
import com.lsxy.framework.tenant.service.TenantService;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.apicertificate.service.ApiCertificateChangeLogService;
import com.lsxy.yuhuni.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class ApiCertificateServiceImpl extends AbstractService<ApiCertificate> implements ApiCertificateService {
    private static final String CERT_CHANGE_TYPE_UUID = "uuid";
    private static final int SK_CHANGE_COUNT_OF_DAY = 3;

    @Autowired
    private ApiCertificateDao apiCertificateDao;

    @Autowired
    private ApiCertificateChangeLogService apiCertificateChangeLogService;

    @Autowired
    private AccountService accountService;
    @Autowired
    private TenantService tenantService;

    @Override
    public BaseDaoInterface<ApiCertificate, Serializable> getDao() {
        return this.apiCertificateDao;
    }

    @Override
    public ApiCertificate findApiCertificateByUserName(String username) throws MatchMutiEntitiesException {
        ApiCertificate apiCertificate = null;
        Tenant tenant = tenantService.findTenantByUserName(username);
        if(tenant != null){
            apiCertificate = findApiCertificateByTenantId(tenant.getId());
        }
        return apiCertificate;
    }

    @Override
    public ApiCertificate findApiCertificateByTenantId(String tenantId) {
        String hql = "from ApiCertificate obj where obj.tenant.id=?1";
        List<ApiCertificate> list = this.findByCustomWithParams(hql, tenantId);
        if(list != null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public String changeSecretKeyByUserName(String userName) throws MatchMutiEntitiesException {
        ApiCertificate cert = findApiCertificateByUserName(userName);
        Long count = apiCertificateChangeLogService.countTodayCertChangeLogByCert(cert);
        if(count < SK_CHANGE_COUNT_OF_DAY){
            String secretKey = UUID.randomUUID().toString();
            apiCertificateChangeLogService.insertApiCertificateChangeLog(cert,secretKey,CERT_CHANGE_TYPE_UUID);
            cert.setSecretKey(secretKey);
            this.save(cert);
            return secretKey;
        }else{
            throw new RuntimeException("当天修改已达到上限");
        }
    }
}
