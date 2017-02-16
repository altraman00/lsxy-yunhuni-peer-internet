package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.apicertificate.dao.CertAccountQuotaDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2017/2/15.
 */
public class CertAccountQuotaServiceImpl extends AbstractService<CertAccountQuota> implements CertAccountQuotaService {
    @Autowired
    CertAccountQuotaDao certAccountQuotaDao;

    @Override
    public BaseDaoInterface<CertAccountQuota, Serializable> getDao() {
        return null;
    }

    @Override
    public void updateQuotas(String certAccountId, List<CertAccountQuota> quotas) {
        if(quotas != null){
            for(CertAccountQuota quota : quotas){
                certAccountQuotaDao.updateQuota(certAccountId,quota.getType(),quota.getValue());
            }
        }
    }

    @Override
    public List<CertAccountQuota> findByCertAccountId(String id) {
        return certAccountQuotaDao.findByCertAccountId(id);
    }

    @Override
    public List<CertAccountQuota> findByAppId(String appId) {
        return certAccountQuotaDao.findByCertAccountId(appId);
    }
}
