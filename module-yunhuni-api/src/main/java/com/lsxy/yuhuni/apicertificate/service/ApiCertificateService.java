package com.lsxy.yuhuni.apicertificate.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.service.BaseService;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificate;

/**
 * 鉴权账号（凭证）相关接口
 * Created by liups on 2016/6/29.
 */
public interface ApiCertificateService extends BaseService<ApiCertificate> {
    /**
     * 根据用户名来查找用户鉴权账号（凭证）
     * @param username 用户名
     * @return 鉴权账号（凭证）
     */
    ApiCertificate findApiCertificateByUserName(String username) throws MatchMutiEntitiesException;


    /**
     * 根据租户ID来查找用户鉴权账号（凭证）
     * @param tenantId 租户ID
     * @return 鉴权账号（凭证）
     */
    ApiCertificate findApiCertificateByTenantId(String tenantId);


    /**
     * 根据用户名来改变用户鉴权账号（凭证）的secretKey
     * @param userName 用户名
     * @return 新生成的secretKey
     */
    String changeSecretKeyByUserName(String userName) throws MatchMutiEntitiesException;
}
