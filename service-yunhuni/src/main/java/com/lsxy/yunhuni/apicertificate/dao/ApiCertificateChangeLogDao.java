package com.lsxy.yunhuni.apicertificate.dao;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificateChangeLog;

import java.io.Serializable;
import java.sql.Date;

/**
 * 鉴权账号（凭证）变更记录查询dao
 * Created by liups on 2016/6/29.
 */
public interface ApiCertificateChangeLogDao extends BaseDaoInterface<ApiCertificateChangeLog, Serializable> {
    Long countByApiCertificateAndChangeDateBetween(ApiCertificate cert, Date start,Date end);
}
