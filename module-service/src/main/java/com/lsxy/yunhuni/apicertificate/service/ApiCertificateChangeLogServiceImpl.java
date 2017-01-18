package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateChangeLog;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateChangeLogService;
import com.lsxy.yunhuni.apicertificate.dao.ApiCertificateChangeLogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class ApiCertificateChangeLogServiceImpl extends AbstractService<ApiCertificateChangeLog> implements ApiCertificateChangeLogService {
    @Autowired
    ApiCertificateChangeLogDao apiCertificateChangeLogDao;
    @Override
    public BaseDaoInterface<ApiCertificateChangeLog, Serializable> getDao() {
        return this.apiCertificateChangeLogDao;
    }


    @Override
    public void insertApiCertificateChangeLog(String certId, String secretKey, String changeType) {
        ApiCertificateChangeLog log = new ApiCertificateChangeLog(certId,secretKey,changeType);
        this.save(log);
    }

    @Override
    public Long countTodayCertChangeLogByCert(ApiCertificate cert) {
        String date = DateUtils.getDate("yyyy-MM-dd");
        Date today = DateUtils.parseDate(date);
        Date nextDate = DateUtils.nextDate(today);
        return apiCertificateChangeLogDao.countByCertIdAndChangeDateBetween(cert.getId(),today,nextDate);
    }
}
