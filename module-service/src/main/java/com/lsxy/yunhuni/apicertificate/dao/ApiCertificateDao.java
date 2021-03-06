package com.lsxy.yunhuni.apicertificate.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;

import java.io.Serializable;

/**
 * 鉴权账号（凭证）查询dao
 * Created by liups on 2016/6/29.
 */
public interface ApiCertificateDao extends BaseDaoInterface<ApiCertificate, Serializable> {

    /**
     * 根据certId获取对应的鉴权账号
     * @param certId
     * @return
     */
    public ApiCertificate findByCertId(String certId);
}
