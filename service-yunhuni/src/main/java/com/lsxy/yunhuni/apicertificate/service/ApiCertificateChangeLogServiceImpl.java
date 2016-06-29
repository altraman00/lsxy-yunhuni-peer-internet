package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificateChangeLog;
import com.lsxy.yuhuni.apicertificate.service.ApiCertificateChangeLogService;
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
    public void insertApiCertificateChangeLog(ApiCertificate certificate, String secretKey, String changeType) {
        ApiCertificateChangeLog log = new ApiCertificateChangeLog(certificate,secretKey,changeType);
        this.save(log);
    }

    @Override
    public Long countTodayCertChangeLogByCert(ApiCertificate cert) {
        String date = DateUtils.getDate("yyyy-MM-dd");
        Date today = DateUtils.parseDate(date);
        Date nextDate = DateUtils.nextDate(today);
        return apiCertificateChangeLogDao.countByApiCertificateAndChangeDateBetween(cert,
                new java.sql.Date(today.getTime()),
                new java.sql.Date(nextDate.getTime()));
    }
}
