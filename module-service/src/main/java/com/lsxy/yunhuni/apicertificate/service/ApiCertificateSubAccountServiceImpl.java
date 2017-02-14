package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateSubAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2017/2/14.
 */
@Service
public class ApiCertificateSubAccountServiceImpl extends AbstractService<ApiCertificateSubAccount> implements ApiCertificateSubAccountService {
    @Autowired
    private ApiCertificateSubAccountDao apiCertificateSubAccountDao;

    @Override
    public BaseDaoInterface<ApiCertificateSubAccount, Serializable> getDao() {
        return this.apiCertificateSubAccountDao;
    }
}
