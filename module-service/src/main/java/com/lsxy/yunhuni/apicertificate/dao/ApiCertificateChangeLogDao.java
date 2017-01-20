package com.lsxy.yunhuni.apicertificate.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateChangeLog;

import java.io.Serializable;
import java.util.Date;

/**
 * 鉴权账号（凭证）变更记录查询dao
 * Created by liups on 2016/6/29.
 */
public interface ApiCertificateChangeLogDao extends BaseDaoInterface<ApiCertificateChangeLog, Serializable> {
    /**
     * 获取一定时间段内的secretKey变更记录的次数
     * @param certId 鉴权账号（凭证）
     * @param start 开始时间
     * @param end 结束时间
     * @return
     */
    Long countByCertIdAndChangeDateBetween(String certId, Date start, Date end);
}
