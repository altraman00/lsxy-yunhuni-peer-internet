package com.lsxy.yunhuni.apicertificate.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

/**
 * Created by liups on 2017/2/14.
 */
public interface ApiCertificateSubAccountDao extends BaseDaoInterface<ApiCertificateSubAccount, Serializable> {
}
