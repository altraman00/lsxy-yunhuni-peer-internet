package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateChangeLogService;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.yunhuni.api.exceptions.SkChangeCountLimitException;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

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
        String hql = "from ApiCertificate obj where obj.tenantId=?1 and obj.type=" + ApiCertificate.TYPE_PRIMARY_ACCOUNT;
        List<ApiCertificate> list = this.findByCustomWithParams(hql, tenantId);
        if(list != null && list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_cert_' + #cert.certId", beforeInvocation = true)
            }
    )
    @Override
    public String changeSecretKey(ApiCertificate cert) throws MatchMutiEntitiesException {
        Long count = apiCertificateChangeLogService.countTodayCertChangeLogByCert(cert);
        if(count < SK_CHANGE_COUNT_OF_DAY){
            String secretKey = UUIDGenerator.uuid();
            apiCertificateChangeLogService.insertApiCertificateChangeLog(cert.getId(),secretKey,CERT_CHANGE_TYPE_UUID);
            cert.setSecretKey(secretKey);
            this.save(cert);
            return secretKey;
        }else{
            throw new SkChangeCountLimitException("当天修改已达到上限");
        }
    }

    @Cacheable(value="entity",key="'entity_cert_'+#certId",unless = "#result == null")
    @Override
    public ApiCertificate findApiCertificateSecretKeyByCertId(String certId) {
        ApiCertificate ac = this.apiCertificateDao.findByCertId(certId);
        return ac;
    }
}
