package com.lsxy.yunhuni.api.apicertificate.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateChangeLog;

/**
 * 鉴权账号（凭证）修改日志接口
 * Created by liups on 2016/6/29.
 */
public interface ApiCertificateChangeLogService extends BaseService<ApiCertificateChangeLog> {

    /**
     * 插入更改日志
     * @param certId 鉴权账号Id（凭证）
     * @param secretKey 更改后的secretKey
     * @param changeType 更改类型
     */
    void insertApiCertificateChangeLog(String certId,String secretKey,String changeType);


    /**
     *
     * @param cert 鉴权账号（凭证）
     * @return 记录数
     */
    Long countTodayCertChangeLogByCert(ApiCertificate cert);
}
